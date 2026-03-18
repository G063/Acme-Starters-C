package acme.features.spokesperson.campaign;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.client.helpers.MomentHelper;
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

		status = this.campaign != null && this.campaign.getDraftMode() && this.campaign.getSpokesperson().isPrincipal();

		super.setAuthorised(status);
	}

	@Override
	public void bind() {
		super.bindObject(this.campaign, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo");
	}

	@Override
	public void validate() {
		super.validateObject(this.campaign);
		Date now = MomentHelper.getBaseMoment();
		boolean validStartMoment = this.campaign.getStartMoment() != null && !this.campaign.getStartMoment().before(now);
		super.state(validStartMoment, "startMoment", "acme.validation.campaign.future-dates.message");
		int milestoneCount = this.repository.countMilestonesByCampaignId(this.campaign.getId());
		super.state(milestoneCount > 0, "*", "acme.validation.campaign.no-milestones");
	}

	@Override
	public void unbind() {
		super.unbindObject(this.campaign, "id","ticker", "name", "description", "startMoment", "endMoment","moreInfo", "draftMode");
		super.unbindGlobal("monthsActive", this.campaign.getMonthsActive());
		Double effort = this.repository.sumEffortByCampaignId(this.campaign.getId());
		super.unbindGlobal("effort", effort == null ? 0.0 : effort);
	}
	
	@Override
	public void execute() {
		this.campaign.setDraftMode(false);
		this.repository.save(this.campaign);
	}
}