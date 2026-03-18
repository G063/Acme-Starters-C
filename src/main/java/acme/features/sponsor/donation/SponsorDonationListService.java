
package acme.features.sponsor.donation;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.entities.sponsorship.Donation;
import acme.entities.sponsorship.Sponsorship;
import acme.realms.Sponsor;

@Service
public class SponsorDonationListService extends AbstractService<Sponsor, Donation> {

	@Autowired
	private SponsorDonationRepository	repository;

	private Collection<Donation>		donations;
	private Sponsorship					sponsorship;


	@Override
	public void load() {
		int sponsorshipId;

		sponsorshipId = super.getRequest().getData("sponsorshipId", int.class);
		this.sponsorship = this.repository.findSponsorshipById(sponsorshipId);
		this.donations = this.repository.findDonationsBySponsorshipId(sponsorshipId);
	}

	@Override
	public void authorise() {
		boolean status;
		boolean isSponsor;
		int sponsorId;

		isSponsor = this.getRequest().getPrincipal().hasRealmOfType(Sponsor.class);
		if (!isSponsor || this.sponsorship == null)
			status = false;
		else {
			sponsorId = this.getRequest().getPrincipal().getActiveRealm().getId();
			status = this.sponsorship.getSponsor() != null && this.sponsorship.getSponsor().getId() == sponsorId;
		}

		super.setAuthorised(status);
	}

	@Override
	public void unbind() {
		super.unbindObjects(this.donations, "name", "kind", "money", "notes");

		super.unbindGlobal("sponsorshipId", this.sponsorship == null ? null : this.sponsorship.getId());
		super.unbindGlobal("sponsorshipDraftMode", this.sponsorship != null && Boolean.TRUE.equals(this.sponsorship.getDraftMode()));
	}
}
