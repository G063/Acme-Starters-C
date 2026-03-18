package acme.features.spokesperson.milestone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.components.views.SelectChoices;
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
		Integer campaignId;
		Campaign campaign;

		campaignId = super.getRequest().getData("campaignId", Integer.class);
		campaign = campaignId == null ? null : this.repository.findCampaignById(campaignId);

		this.milestone = this.newObject(Milestone.class);
		this.milestone.setCampaign(campaign);
	}

	@Override
	public void authorise() {
		boolean status;
		int spokespersonId;

		spokespersonId = super.getRequest().getPrincipal().getActiveRealm().getId();
		
		status = super.getRequest().getPrincipal().hasRealmOfType(Spokesperson.class) && 
				this.milestone.getCampaign() != null && 
				this.milestone.getCampaign().getSpokesperson().getId() == spokespersonId &&
				this.milestone.getCampaign().getDraftMode();

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
		SelectChoices choices;
		Campaign campaign;

		choices = SelectChoices.from(MilestoneKind.class, this.milestone.getKind());
		tuple = super.unbindObject(this.milestone, "title", "achievements", "effort", "kind");
		campaign = this.milestone.getCampaign();

		tuple.put("kinds", choices);
		tuple.put("campaignId", campaign == null ? null : campaign.getId());
		tuple.put("draftMode", campaign != null && campaign.getDraftMode());
	}
}
