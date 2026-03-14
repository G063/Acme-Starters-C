package acme.features.sponsor.donation;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.realms.Sponsor;
import acme.client.services.AbstractService;
import acme.entities.sponsorship.Donation;
import acme.entities.sponsorship.Sponsorship;

@Service
public class SponsorDonationListService extends AbstractService<Sponsor, Donation> {

	@Autowired
	private SponsorDonationRepository repository;

	private Collection<Donation> donations;
	private Sponsorship sponsorship;


	@Override
	public void load() {
		int sponsorshipId;

		sponsorshipId = super.getRequest().getData("sponsorshipId", int.class);
		this.sponsorship = this.repository.findSponsorshipById(sponsorshipId);
		this.donations = this.repository.findDonationsBySponsorshipId(sponsorshipId);
	}

	@Override
	public void authorise() {
		int sponsorshipId;
		int userAccountId;
		Long count;
		boolean status;

		sponsorshipId = super.getRequest().getData("sponsorshipId", int.class);
		userAccountId = super.getRequest().getPrincipal().getAccountId();
		count = this.repository.countOwnedSponsorshipById(sponsorshipId, userAccountId);
		status = count != null && count > 0;
		super.setAuthorised(status);
	}

	@Override
	public void unbind() {
		for (Donation donation : this.donations)
			super.unbindObject(donation, "name", "kind", "money", "notes");

		super.unbindGlobal("sponsorshipId", this.sponsorship == null ? null : this.sponsorship.getId());
		super.unbindGlobal("sponsorshipDraftMode", this.sponsorship != null && Boolean.TRUE.equals(this.sponsorship.getDraftMode()));
	}
}

