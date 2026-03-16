package acme.features.any.sponsorship;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.principals.Any;
import acme.client.services.AbstractService;
import acme.entities.sponsorship.Sponsorship;
import acme.entities.sponsorship.SponsorshipRepository;

@Service
public class AnySponsorshipListService extends AbstractService<Any, Sponsorship> {

	@Autowired
	private SponsorshipRepository	repository;

	private Collection<Sponsorship>	sponsorships;


	@Override
	public void load() {
		this.sponsorships = this.repository.findPublishedSponsorships();
	}

	@Override
	public void authorise() {
		super.setAuthorised(true);
	}

	@Override
	public void unbind() {
		for (Sponsorship sponsorship : this.sponsorships)
			super.unbindObject(sponsorship, "ticker", "name", "startMoment", "endMoment", "sponsor.im");
	}
}
