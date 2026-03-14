package acme.features.any.spokesperson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.principals.Any;
import acme.client.services.AbstractService;
import acme.entities.campaign.Campaign;
import acme.features.any.campaign.AnyCampaignRepository;
import acme.realms.Spokesperson;

@Service
public class AnySpokespersonShowService extends AbstractService<Any, Spokesperson> {

	@Autowired
	private AnySpokespersonRepository	repository;

	@Autowired
	private AnyCampaignRepository		campaignRepository;

	private Spokesperson				spokesperson;
	private Campaign					campaign;


	@Override
	public void load() {
		int campaignId;

		campaignId = super.getRequest().getData("campaignId", int.class);
		this.campaign = this.campaignRepository.findCampaignById(campaignId);
		this.spokesperson = this.repository.findSpokespersonById(this.campaign.getSpokesperson().getId());
	}

	@Override
	public void authorise() {
		boolean status;

		status = this.campaign != null && !this.campaign.getDraftMode();

		super.setAuthorised(status);
	}

	@Override
	public void unbind() {
		super.unbindObject(this.spokesperson,"identity.name","identity.surname", "cv", "achievements", "licensed");
	}
}