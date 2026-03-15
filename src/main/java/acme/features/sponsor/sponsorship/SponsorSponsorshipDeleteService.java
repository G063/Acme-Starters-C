
package acme.features.sponsor.sponsorship;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.entities.sponsorship.Donation;
import acme.entities.sponsorship.Sponsorship;
import acme.realms.Sponsor;

@Service
public class SponsorSponsorshipDeleteService extends AbstractService<Sponsor, Sponsorship> {

	@Autowired
	private SponsorSponsorshipRepository	repository;

	private Sponsorship						sponsorship;


	@Override
	public void load() {
		int id;

		id = super.getRequest().getData("id", int.class);
		this.sponsorship = this.repository.findSponsorshipById(id);
	}

	@Override
	public void authorise() {
		int sponsorId;
		boolean isOwner;
		boolean isDraft;

		sponsorId = this.getRequest().getPrincipal().getActiveRealm().getId();

		isOwner = this.sponsorship != null && this.sponsorship.getSponsor().getId() == sponsorId;

		isDraft = this.sponsorship != null && this.sponsorship.getDraftMode();

		super.setAuthorised(isOwner && isDraft);

	}

	@Override
	public void bind() {
		super.bindObject(this.sponsorship, "id");
	}

	@Override
	public void validate() {
		super.state(this.sponsorship != null, "*", "sponsor.sponsorship.error.not-found");
	}

	@Override
	public void execute() {
		Collection<Donation> donations;

		donations = this.repository.findDonationsBySponsorshipId(this.sponsorship.getId());
		this.repository.deleteAll(donations);
		this.repository.delete(this.sponsorship);

		super.getResponse().setView("redirect:/sponsor/sponsorship/list");
	}

	@Override
	public void unbind() {
		super.getResponse().addGlobal("confirmation", "sponsor.sponsorship.delete.success");
	}
}
