package acme.features.sponsor.sponsorship;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.realms.Sponsor;
import acme.client.services.AbstractService;
import acme.entities.sponsorship.Sponsorship;

@Service
public class SponsorSponsorshipListService extends AbstractService<Sponsor, Sponsorship> {

	@Autowired
	private SponsorSponsorshipRepository repository;

	private Collection<Sponsorship> sponsorships;


	@Override
	public void load() {
		int userAccountId;

		userAccountId = super.getRequest().getPrincipal().getAccountId();
		this.sponsorships = this.repository.findSponsorshipsByUserAccountId(userAccountId);
	}

	@Override
	public void authorise() {
		super.setAuthorised(true);
	}

	@Override
	public void unbind() {
		for (Sponsorship sponsorship : this.sponsorships)
			super.unbindObject(sponsorship, "ticker", "name", "startMoment", "endMoment", "draftMode", "totalMoney");
	}
}

