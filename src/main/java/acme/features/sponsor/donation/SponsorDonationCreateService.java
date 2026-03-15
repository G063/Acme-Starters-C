
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


	@Override
	public void load() {
		int sponsorshipId;
		Sponsorship sponsorship;

		sponsorshipId = super.getRequest().getData("sponsorshipId", int.class);
		sponsorship = this.repository.findSponsorshipById(sponsorshipId);

		this.donation = this.newObject(Donation.class);
		this.donation.setSponsorship(sponsorship);
	}

	@Override
	public void authorise() {
		super.setAuthorised(this.getRequest().getPrincipal().hasRealmOfType(Sponsor.class));
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
		tuple.put("sponsorshipId", this.donation.getSponsorship().getId());
		tuple.put("sponsorshipDraftMode", this.donation.getSponsorship() != null && Boolean.TRUE.equals(this.donation.getSponsorship().getDraftMode()));
	}
}
