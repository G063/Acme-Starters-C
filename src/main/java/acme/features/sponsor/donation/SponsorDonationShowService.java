
package acme.features.sponsor.donation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractService;
import acme.datatypes.DonationKind;
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
		int sponsorId;
		boolean isOwner;
		boolean isDraft;

		isDraft = this.donation != null && this.donation.getSponsorship() != null && Boolean.TRUE.equals(this.donation.getSponsorship().getDraftMode());

		if (this.getRequest().getPrincipal() == null || !this.getRequest().getPrincipal().hasRealmOfType(Sponsor.class))
			isOwner = false;
		else {
			sponsorId = this.getRequest().getPrincipal().getActiveRealm().getId();

			isOwner = this.donation != null && this.donation.getSponsorship() != null && this.donation.getSponsorship().getSponsor() != null && this.donation.getSponsorship().getSponsor().getId() == sponsorId;
		}

		super.setAuthorised(isOwner && isDraft);
	}

	@Override
	public void unbind() {
		Tuple tuple;
		var sponsorship = this.donation.getSponsorship();

		tuple = super.unbindObject(this.donation, "name", "kind", "money", "notes");

		tuple.put("kinds", SelectChoices.from(DonationKind.class, this.donation.getKind()));
		tuple.put("sponsorshipId", sponsorship == null ? null : sponsorship.getId());
		tuple.put("sponsorshipDraftMode", sponsorship != null && Boolean.TRUE.equals(sponsorship.getDraftMode()));
	}
}
