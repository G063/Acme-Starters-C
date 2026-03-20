
package acme.features.any.donation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.principals.Any;
import acme.client.services.AbstractService;
import acme.entities.sponsorship.Donation;

@Service
public class AnyDonationShowService extends AbstractService<Any, Donation> {

	@Autowired
	private AnyDonationRepository	repository;
	private Donation				donation;


	@Override
	public void load() {
		int donationId;

		donationId = super.getRequest().getData("id", int.class);
		this.donation = this.repository.findDonationById(donationId);
	}

	@Override
	public void authorise() {
		super.setAuthorised(this.donation != null && this.donation.getSponsorship() != null && Boolean.FALSE.equals(this.donation.getSponsorship().getDraftMode()));
	}

	@Override
	public void unbind() {
		if (this.donation != null)
			super.unbindObject(this.donation, "name", "kind", "money", "notes");
	}
}
