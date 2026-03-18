
package acme.features.any.tactic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.principals.Any;
import acme.client.services.AbstractService;
import acme.entities.strategy.Tactic;

@Service
public class AnyTacticShowService extends AbstractService<Any, Tactic> {

	@Autowired
	protected AnyTacticRepository	repository;

	protected Tactic				tactic;


	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		this.tactic = this.repository.findTacticById(id);
	}

	@Override
	public void authorise() {
		super.setAuthorised(this.tactic != null && !this.tactic.getStrategy().getDraftMode());
	}

	@Override
	public void unbind() {
		if (this.tactic != null)
			super.unbindObject(this.tactic, "id", "name", "notes", "expectedPercentage", "kind", "strategy.ticker");
	}
}
