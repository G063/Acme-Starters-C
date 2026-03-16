package acme.features.spokesperson.campaign;

import java.util.Date;

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
		super.bindObject(this.campaign, "ticker", "name", "description", "moreInfo");
		Date start = this.getRequest().getData("startMoment", Date.class);
		Date end = this.getRequest().getData("endMoment", Date.class);
		this.campaign.setStartMoment(start);
		this.campaign.setEndMoment(end);
	}

	@Override
	public void validate() {
		super.validateObject(this.campaign);
	}


	@Override
	public void unbind() {

		super.unbindObject(this.campaign, "id","ticker", "name", "description", "startMoment", "endMoment","moreInfo", "draftMode");
	}
	
	@Override
	public void execute() {
		this.repository.save(this.campaign);
	}
	
	
}
