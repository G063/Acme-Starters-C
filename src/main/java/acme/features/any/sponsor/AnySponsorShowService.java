package acme.features.any.sponsor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.components.principals.Any;
import acme.client.services.AbstractService;
import acme.realms.Sponsor;

@Service
public class AnySponsorShowService extends AbstractService<Any, Sponsor> {

	@Autowired
	private AnySponsorRepository	repository;

	private Sponsor				sponsor;
	private int					sponsorshipId;


	@Override
	public void load() {
		int sponsorId;

		sponsorId = super.getRequest().getData("id", int.class);
		this.sponsorshipId = super.getRequest().getData("sponsorshipId", int.class);
		this.sponsor = this.repository.findSponsorById(sponsorId);
	}

	@Override
	public void authorise() {
		int sponsorId;
		int sponsorshipId;
		Long count;
		boolean status;

		sponsorId = super.getRequest().getData("id", int.class);
		sponsorshipId = super.getRequest().getData("sponsorshipId", int.class);
		count = this.repository.countPublishedSponsorshipBySponsor(sponsorshipId, sponsorId);
		status = count != null && count > 0;
		super.setAuthorised(status);
	}

	@Override
	public void unbind() {
		Tuple tuple;

		tuple = super.unbindObject(this.sponsor, "identity.name", "identity.surname", "identity.email", "address", "im", "gold");
		tuple.put("sponsorshipId", this.sponsorshipId);
	}
}
