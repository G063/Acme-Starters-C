package acme.features.any.donation;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.principals.Any;
import acme.client.services.AbstractService;
import acme.entities.sponsorship.Donation;

@Service
public class AnyDonationListService extends AbstractService<Any, Donation> {

	@Autowired
	private AnyDonationRepository	repository;

	private Collection<Donation>	donations;


	@Override
	public void load() {
		int sponsorshipId;

		sponsorshipId = super.getRequest().getData("sponsorshipId", int.class);
		this.donations = this.repository.findPublishedDonationsBySponsorshipId(sponsorshipId);
	}

	@Override
	public void authorise() {
		int sponsorshipId;
		Long count;
		boolean status;

		sponsorshipId = super.getRequest().getData("sponsorshipId", int.class);
		count = this.repository.countPublishedSponsorshipById(sponsorshipId);
		status = count != null && count > 0;
		super.setAuthorised(status);
	}

	@Override
	public void unbind() {
		for (Donation donation : this.donations)
			super.unbindObject(donation, "name", "kind", "money", "notes");
	}
}
