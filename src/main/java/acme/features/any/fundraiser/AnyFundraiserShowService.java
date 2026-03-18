
package acme.features.any.fundraiser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.components.principals.Any;
import acme.client.services.AbstractService;
import acme.realms.Fundraiser;

@Service
public class AnyFundraiserShowService extends AbstractService<Any, Fundraiser> {

	@Autowired
	private AnyFundraiserRepository	repository;

	private Fundraiser				fundraiser;
	private int						strategyId;


	@Override
	public void load() {
		int fundraiserId = super.getRequest().getData("id", int.class);
		this.strategyId = super.getRequest().getData("strategyId", int.class);

		this.fundraiser = this.repository.findFundraiserById(fundraiserId);
	}

	@Override
	public void authorise() {
		int fundraiserId = super.getRequest().getData("id", int.class);
		int stratId = super.getRequest().getData("strategyId", int.class);

		Long count = this.repository.countPublishedStrategyByFundraiser(stratId, fundraiserId);
		boolean status = count != null && count > 0;

		super.setAuthorised(status);
	}

	@Override
	public void unbind() {
		Tuple tuple;

		tuple = super.unbindObject(this.fundraiser, "userAccount.identity.name", "userAccount.identity.surname", "userAccount.identity.email", "bank", "statement", "agent");

		tuple.put("strategyId", this.strategyId);
	}
}
