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
public class SpokespersonMilestoneShowService extends AbstractService<Spokesperson, Milestone> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected SpokespersonMilestoneRepository repository;

	protected Milestone milestone;

	// AbstractService interface -------------------------------------------

	@Override
	public void load() {
		int id;

		id = super.getRequest().getData("id", int.class);
		this.milestone = this.repository.findMilestoneById(id);
	}

	@Override
	public void authorise() {
		boolean status;
		Campaign campaign;

		campaign = this.milestone.getCampaign();
		
		status =  campaign.getSpokesperson().isPrincipal() || !campaign.getDraftMode();

		super.setAuthorised(status);
	}

	@Override
	public void unbind() {
		Tuple tuple;
		SelectChoices choices;

		choices = SelectChoices.from(MilestoneKind.class, this.milestone.getKind());
		tuple = super.unbindObject(this.milestone, "title", "achievements", "effort", "kind");

		tuple.put("kinds", choices);
		tuple.put("campaignId", this.milestone.getCampaign().getId());
		tuple.put("draftMode", this.milestone.getCampaign().getDraftMode());
	}
}
