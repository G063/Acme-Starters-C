
package acme.features.sponsor.donation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractService;
import acme.datatypes.DonationKind;
import acme.entities.sponsorship.Donation;
import acme.entities.sponsorship.Sponsorship;
import acme.realms.Sponsor;

@Service
public class SponsorDonationCreateService extends AbstractService<Sponsor, Donation> {

	@Autowired
	private SponsorDonationRepository	repository;

	private Donation					donation;
	private int							sponsorshipId;


	@Override
	public void load() {
		Sponsorship sponsorship;

		this.sponsorshipId = super.getRequest().getData("sponsorshipId", int.class);
		sponsorship = this.repository.findSponsorshipById(this.sponsorshipId);

		this.donation = this.newObject(Donation.class);
		this.donation.setSponsorship(sponsorship);
	}

	@Override
	public void authorise() {
		int userAccountId;
		Long count;
		boolean status;

		userAccountId = super.getRequest().getPrincipal().getAccountId();
		count = this.repository.countOwnedDraftSponsorshipById(this.sponsorshipId, userAccountId);
		status = count != null && count > 0;
		super.setAuthorised(status);
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
		this.repository.save(this.donation);
	}

	@Override
	public void unbind() {
		Tuple tuple;

		tuple = super.unbindObject(this.donation, "name", "kind", "money", "notes");
		tuple.put("kinds", SelectChoices.from(DonationKind.class, this.donation.getKind()));
		tuple.put("sponsorshipId", this.sponsorshipId);
		tuple.put("sponsorshipDraftMode", this.donation.getSponsorship() != null && Boolean.TRUE.equals(this.donation.getSponsorship().getDraftMode()));
	}
}
