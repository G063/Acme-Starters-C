package acme.features.any.spokesperson;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.components.principals.Any;
import acme.client.services.AbstractService;
import acme.realms.Spokesperson;

@Service
public class AnySpokespersonShowService extends AbstractService<Any, Spokesperson> {

	@Autowired
	private AnySpokespersonRepository	repository;

	private Spokesperson				spokesperson;
	private int						campaignId;


	@Override
	public void load() {
		int spokespersonId = super.getRequest().getData("id", int.class);
		this.campaignId = super.getRequest().getData("campaignId", int.class);
		this.spokesperson = this.repository.findSpokespersonById(spokespersonId);
	}

	@Override
	public void authorise() {
		int spokespersonId = super.getRequest().getData("id", int.class);
		int campainId = super.getRequest().getData("campaignId", int.class);
		Long count = this.repository.countPublishedCampaignBySpokesperson(campainId, spokespersonId);
		boolean status = count != null && count > 0;
		super.setAuthorised(status);
	}

	@Override
	public void unbind() {
		Tuple tuple;
		tuple =super.unbindObject(this.spokesperson, "userAccount.identity.name", "userAccount.identity.surname", "userAccount.identity.email", "cv", "achievements", "licensed");
		tuple.put("campaignId", this.campaignId);
	}
}