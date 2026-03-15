
package acme.features.any.donation;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Any;
import acme.client.services.AbstractService;
import acme.entities.sponsorship.Donation;

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
		super.setAuthorised(true);
	}

	@Override
	public void unbind() {
		super.unbindObject(this.donation, "name", "kind", "money", "notes");
	}
}
