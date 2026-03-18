package acme.features.spokesperson.milestone;

import java.util.Collections;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.entities.campaign.Campaign;
import acme.entities.campaign.Milestone;
import acme.realms.Spokesperson;

@Service
public class SpokespersonMilestoneListService extends AbstractService<Spokesperson, Milestone> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected SpokespersonMilestoneRepository repository;

	protected Campaign					campaign;
	protected Collection<Milestone>		milestones;

	// AbstractService interface -------------------------------------------

	@Override
	public void load() {
		Integer campaignId;

		campaignId = super.getRequest().getData("campaignId", Integer.class);
		
		this.campaign = campaignId == null ? null : this.repository.findCampaignById(campaignId);
		this.milestones = this.campaign == null ? Collections.emptyList() : this.repository.findMilestonesByCampaignId(campaignId);
	}

	@Override
	public void authorise() {
		boolean status;
		status = this.campaign != null && (!this.campaign.getDraftMode() || this.campaign.getSpokesperson().isPrincipal());
		super.setAuthorised(status);
	}

	@Override
	public void unbind() {
		boolean showCreate;
		super.unbindObjects(this.milestones, "title", "achievements", "effort", "kind");
		showCreate = this.campaign != null && this.campaign.getDraftMode() && this.campaign.getSpokesperson().isPrincipal();
		super.unbindGlobal("campaignId", this.campaign == null ? null : this.campaign.getId());
		super.unbindGlobal("showCreate", showCreate);
	}
}
