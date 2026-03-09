
package acme.features.any.tactic;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.principals.Any;
import acme.client.services.AbstractService;
import acme.entities.strategy.Strategy;
import acme.entities.strategy.Tactic;

@Service
public class AnyTacticListService extends AbstractService<Any, Tactic> {

	@Autowired
	protected AnyTacticRepository	repository;

	protected Collection<Tactic>	tactics;
	protected Strategy				strategy;


	@Override
	public void load() {
		Integer strategyId;

		strategyId = super.getRequest().getData("strategyId", Integer.class);
		this.strategy = this.repository.findStrategyById(strategyId);
		this.tactics = this.repository.findTacticsByStrategyId(strategyId);
	}

	@Override
	public void authorise() {
		boolean status;

		status = this.strategy != null && !this.strategy.getDraftMode();

		super.setAuthorised(status);
	}

	@Override
	public void unbind() {
		super.unbindObjects(this.tactics, "name", "expectedPercentage", "kind");
	}
}
