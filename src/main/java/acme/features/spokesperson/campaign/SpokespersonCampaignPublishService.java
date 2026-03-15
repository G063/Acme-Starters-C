package acme.features.spokesperson.campaign;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.entities.campaign.Campaign;
import acme.realms.Spokesperson;

@Service
public class SpokespersonCampaignPublishService extends AbstractService<Spokesperson, Campaign> {

	@Autowired
	private SpokespersonCampaignRepository repository;

	private Campaign campaign;

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
		super.bindObject(this.campaign);
	}

	@Override
	public void validate() {
		super.validateObject(this.campaign);
		int milestoneCount = this.repository.countMilestonesByCampaignId(this.campaign.getId());
		super.state(milestoneCount > 0, "*", "inventor.invention.form.error.no-parts");
	}

	@Override
	public void unbind() {

		super.unbindObject(this.campaign, "id","ticker", "name", "description", "startMoment", "endMoment","moreInfo", "draftMode");
	}
	
	@Override
	public void execute() {
		this.campaign.setDraftMode(false);
		this.repository.save(this.campaign);
	}
}