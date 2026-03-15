
package acme.features.sponsor.donation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.services.AbstractService;
import acme.entities.sponsorship.Donation;
import acme.realms.Sponsor;

@Service
public class SponsorDonationShowService extends AbstractService<Sponsor, Donation> {

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
		super.setAuthorised(this.getRequest().getPrincipal().hasRealmOfType(Sponsor.class));
	}

	@Override
	public void unbind() {
		Tuple tuple;

		tuple = super.unbindObject(this.donation, "name", "kind", "money", "notes");

		tuple.put("sponsorshipId", this.donation.getSponsorship().getId());
		tuple.put("sponsorshipDraftMode", Boolean.TRUE.equals(this.donation.getSponsorship().getDraftMode()));
	}
}
