
package acme.features.sponsor.donation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.services.AbstractService;
import acme.entities.sponsorship.Donation;
import acme.realms.Sponsor;

@Service
public class SponsorDonationDeleteService extends AbstractService<Sponsor, Donation> {

	@Autowired
	private SponsorDonationRepository	repository;

	private Donation					donation;


	@Override
	public void load() {
		int id;

		id = super.getRequest().getData("id", int.class);
		this.donation = this.repository.findDonationById(id);
	}

	@Override
	public void authorise() {
		int sponsorId;
		boolean isOwner;
		boolean isDraft;
		boolean isSponsor;

		sponsorId = this.getRequest().getPrincipal().getActiveRealm().getId();

		isSponsor = this.getRequest().getPrincipal().hasRealmOfType(Sponsor.class);

		isOwner = this.donation != null && this.donation.getSponsorship().getSponsor().getId() == sponsorId;

		isDraft = this.donation != null && this.donation.getSponsorship().getDraftMode();

		super.setAuthorised(isOwner && isDraft && isSponsor);
	}

	@Override
	public void bind() {
		super.bindObject(this.donation, "name", "kind", "money", "notes");
	}

	@Override
	public void validate() {
		super.validateObject(this.donation);
	}

	@Override
	public void execute() {
		this.repository.delete(this.donation);

		super.getResponse().setView("redirect:/sponsor/donation/list?sponsorshipId=" + this.donation.getSponsorship().getId());
	}

	@Override
	public void unbind() {
		Tuple tuple;

		tuple = super.unbindObject(this.donation, "name", "kind", "money", "notes");
		tuple.put("sponsorshipId", this.donation.getSponsorship().getId());
		tuple.put("sponsorshipDraftMode", Boolean.TRUE.equals(this.donation.getSponsorship().getDraftMode()));
	}
}
