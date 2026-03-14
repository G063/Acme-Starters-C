package acme.features.sponsor.donation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.realms.Sponsor;
import acme.client.services.AbstractService;
import acme.entities.sponsorship.Donation;

@Service
public class SponsorDonationDeleteService extends AbstractService<Sponsor, Donation> {

	@Autowired
	private SponsorDonationRepository repository;

	private Donation donation;


	@Override
	public void load() {
		int id;

		id = super.getRequest().getData("id", int.class);
		this.donation = this.repository.findDonationById(id);
	}

	@Override
	public void authorise() {
		int id;
		int userAccountId;
		Long count;
		boolean status;

		id = super.getRequest().getData("id", int.class);
		userAccountId = super.getRequest().getPrincipal().getAccountId();
		count = this.repository.countOwnedDraftDonationById(id, userAccountId);
		status = count != null && count > 0;
		super.setAuthorised(status);
	}

	@Override
	public void bind() {
		;
	}

	@Override
	public void validate() {
		;
	}

	@Override
	public void execute() {
		this.repository.delete(this.donation);
	}

	@Override
	public void unbind() {
		Tuple tuple;

		tuple = super.unbindObject(this.donation, "name", "kind", "money", "notes");
		tuple.put("sponsorshipId", this.donation.getSponsorship().getId());
		tuple.put("sponsorshipDraftMode", Boolean.TRUE.equals(this.donation.getSponsorship().getDraftMode()));
	}
}

