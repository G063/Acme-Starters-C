package acme.features.spokesperson.milestone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.services.AbstractService;
import acme.entities.campaign.Campaign;
import acme.entities.campaign.Milestone;
import acme.entities.campaign.MilestoneKind;
import acme.realms.Spokesperson;

@Service
public class SpokespersonMilestoneCreateService extends AbstractService<Spokesperson, Milestone> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected SpokespersonMilestoneRepository repository;

	protected Milestone milestone;

	// AbstractService interface -------------------------------------------

	@Override
	public void load() {
		int campaignId;
		Campaign campaign;

		campaignId = super.getRequest().getData("campaignId", int.class);
		campaign = this.repository.findCampaignById(campaignId);

		this.milestone = super.newObject(Milestone.class);
		this.milestone.setTitle("");
		this.milestone.setAchievements("");
		this.milestone.setEffort(0.0);
		this.milestone.setKind(MilestoneKind.TEASER);
		this.milestone.setCampaign(campaign);
	}

	@Override
	public void authorise() {
		boolean status;
		Campaign campaign;

		campaign = this.milestone.getCampaign();
		
		status = campaign != null && 
                 campaign.getDraftMode() && 
                 campaign.getSpokesperson().isPrincipal();

		super.setAuthorised(status);
	}

	@Override
	public void bind() {
		super.bindObject(this.milestone, "title", "achievements", "effort", "kind");
	}

	@Override
	public void validate() {
		super.validateObject(this.milestone);
	}

	@Override
	public void execute() {
		this.repository.save(this.milestone);
	}

	@Override
	public void unbind() {
		Tuple tuple;

		tuple = super.unbindObject(this.milestone, "title", "achievements", "effort", "kind");

		tuple.put("campaignId", this.milestone.getCampaign().getId());
		tuple.put("draftMode", this.milestone.getCampaign().getDraftMode());
	}
}
