package acme.features.spokesperson.campaign;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.entities.campaign.Campaign;
import acme.realms.Spokesperson;

@Service
public class SpokespersonCampaignUpdateService extends AbstractService<Spokesperson, Campaign> {

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
	}


	@Override
	public void unbind() {

		super.unbindObject(this.campaign, "id","ticker", "name", "description", "startMoment", "endMoment","moreInfo", "draftMode");
		super.unbindGlobal("monthsActive", this.campaign.getMonthsActive());
		super.unbindGlobal("effort", this.campaign.getEffort());
	}
	
	@Override
	public void execute() {
		this.repository.save(this.campaign);
	}
	
	
}
