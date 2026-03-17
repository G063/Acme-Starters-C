
package acme.features.any.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.principals.Any;
import acme.client.services.AbstractService;
import acme.entities.strategy.Strategy;

@Service
public class AnyStrategyShowService extends AbstractService<Any, Strategy> {

	@Autowired
	protected AnyStrategyRepository	repository;

	protected Strategy				strategy;


	@Override
	public void load() {
		int id;

		id = super.getRequest().getData("id", int.class);
		this.strategy = this.repository.findStrategyById(id);
	}

	@Override
	public void authorise() {
		super.setAuthorised(this.strategy != null && !this.strategy.getDraftMode());
	}

	@Override
	public void unbind() {

		super.unbindObject(this.strategy, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo");
		super.unbindGlobal("monthsActive", this.strategy.getMonthsActive());
		super.unbindGlobal("expectedPercentage", this.strategy.getExpectedPercentage());
		super.getResponse().addGlobal("fundraiserId", this.strategy.getFundraiser().getId());
	}
}
