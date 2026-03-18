
package acme.features.sponsor.donation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractService;
import acme.datatypes.DonationKind;
import acme.entities.sponsorship.Donation;
import acme.realms.Sponsor;

@Service
public class SponsorDonationUpdateService extends AbstractService<Sponsor, Donation> {

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
		boolean status;
		boolean isSponsor;
		boolean isOwner;
		boolean isDraft;
		int sponsorId;

		isSponsor = this.getRequest().getPrincipal().hasRealmOfType(Sponsor.class);
		if (!isSponsor || this.donation == null || this.donation.getSponsorship() == null)
			status = false;
		else {
			sponsorId = this.getRequest().getPrincipal().getActiveRealm().getId();
			isOwner = this.donation.getSponsorship().getSponsor() != null && this.donation.getSponsorship().getSponsor().getId() == sponsorId;
			isDraft = Boolean.TRUE.equals(this.donation.getSponsorship().getDraftMode());
			status = isOwner && isDraft;
		}

		super.setAuthorised(status);
	}

	@Override
	public void bind() {
		super.bindObject(this.donation, "name", "kind", "money", "notes");
	}

	@Override
	public void validate() {
		super.state(this.donation != null && this.donation.getSponsorship() != null, "*", "sponsor.donation.error.not-found");
		super.validateObject(this.donation);

		if (this.donation != null)
			super.getResponse().setView("redirect:/sponsor/donation/show?id=" + this.donation.getId());
	}

	@Override
	public void execute() {
		this.repository.save(this.donation);

		super.getResponse().setView("redirect:/sponsor/donation/show?id=" + this.donation.getId());
	}

	@Override
	public void unbind() {
		Tuple tuple;
		var sponsorship = this.donation.getSponsorship();

		tuple = super.unbindObject(this.donation, "name", "kind", "money", "notes");
		tuple.put("kinds", SelectChoices.from(DonationKind.class, this.donation.getKind()));
		tuple.put("sponsorshipId", sponsorship == null ? null : sponsorship.getId());
		tuple.put("sponsorshipDraftMode", sponsorship != null && Boolean.TRUE.equals(sponsorship.getDraftMode()));
	}
}
