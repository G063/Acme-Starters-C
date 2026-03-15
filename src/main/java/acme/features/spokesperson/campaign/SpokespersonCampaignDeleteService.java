package acme.features.spokesperson.campaign;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.entities.campaign.Campaign;
import acme.entities.campaign.Milestone;
import acme.realms.Spokesperson;

@Service
public class SpokespersonCampaignDeleteService extends AbstractService<Spokesperson, Campaign> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private SpokespersonCampaignRepository repository;

	private Campaign campaign;


	// AbstractService interface ---------------------------------------------

	@Override
	public void load() {
		int id;

		id = super.getRequest().getData("id", int.class);
		this.campaign = this.repository.findCampaignById(id);
	}

	@Override
	public void authorise() {
		boolean status;

		status = super.getRequest().getPrincipal().hasRealmOfType(Spokesperson.class);
		int campaignId = this.getRequest().getPrincipal().getActiveRealm().getId();
		boolean isOwner = this.campaign != null && this.campaign.getSpokesperson().getId() == campaignId;
		boolean isDraft = this.campaign != null && this.campaign.getDraftMode();

		super.setAuthorised(isOwner && isDraft && status);
	}

	@Override
	public void bind() {
		super.bindObject(this.campaign, "id");
	}

	@Override
	public void validate() {
		super.state(this.campaign != null, "*", "inventor.invention.error.not-found");
	}

	@Override
	public void execute() {
		Collection<Milestone> milestones;

		milestones = this.repository.findMilestonesByCampaignId(this.campaign.getId());
		this.repository.deleteAll(milestones);

		this.repository.delete(this.campaign);
	}

	@Override
	public void unbind() {
		super.getResponse().addGlobal("confirmation", "inventor.invention.delete.success");
	}

}