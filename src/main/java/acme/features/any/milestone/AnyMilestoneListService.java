package acme.features.any.milestone;

import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.principals.Any;
import acme.client.services.AbstractService;
import acme.entities.campaign.Campaign;
import acme.entities.campaign.Milestone;

@Service
public class AnyMilestoneListService extends AbstractService<Any, Milestone> {

    @Autowired
    protected AnyMilestoneRepository repository;
	protected Collection<Milestone>	milestone;
	protected Campaign campaign;


	@Override
	public void load() {
		Integer campaignId;
		campaignId = super.getRequest().getData("campaignId", Integer.class);
		this.campaign = this.repository.findCampaignById(campaignId);
		this.milestone = this.repository.findMilestonesByCampaignId(campaignId);
	}

	@Override
	public void authorise() {
		boolean status;

		status = this.campaign != null && !this.campaign.getDraftMode();

		super.setAuthorised(status);
	}

	@Override
	public void unbind() {
		super.unbindObjects(this.milestone, "title", "achievements", "kind", "effort");
	}
}