
package acme.features.fundraiser.tactic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractService;
import acme.entities.strategy.Strategy;
import acme.entities.strategy.Tactic;
import acme.entities.strategy.TacticKind;
import acme.realms.Fundraiser;

@Service
public class FundraiserTacticCreateService extends AbstractService<Fundraiser, Tactic> {

	@Autowired
	protected FundraiserTacticRepository	repository;
	protected Tactic						tactic;
	protected Strategy						strategy;


	@Override
	public void load() {
		int strategyId = super.getRequest().getData("strategyId", int.class);
		this.strategy = this.repository.findOneStrategyById(strategyId);

		this.tactic = super.newObject(Tactic.class);
		this.tactic.setStrategy(this.strategy);
	}

	@Override
	public void authorise() {
		final boolean hasPrincipal = super.getRequest().getPrincipal() != null;
		final boolean status = hasPrincipal && super.getRequest().getPrincipal().hasRealmOfType(Fundraiser.class);
		boolean isOwner = false;
		boolean isDraft = false;

		if (status && this.strategy != null) {
			int fundraiserId = super.getRequest().getPrincipal().getActiveRealm().getId();
			isOwner = this.strategy.getFundraiser().getId() == fundraiserId;
			isDraft = this.strategy.getDraftMode();
		}

		super.setAuthorised(status && isOwner && isDraft);
	}

	@Override
	public void bind() {
		super.bindObject(this.tactic, "name", "notes", "expectedPercentage", "kind");
	}

	@Override
	public void validate() {
		super.validateObject(this.tactic);
	}

	@Override
	public void execute() {
		this.repository.save(this.tactic);
	}

	@Override
	public void unbind() {
		if (this.tactic != null && this.tactic.getStrategy() != null) {
			Tuple tuple;
			SelectChoices choices;

			choices = SelectChoices.from(TacticKind.class, this.tactic.getKind());
			tuple = super.unbindObject(this.tactic, "name", "notes", "expectedPercentage", "kind");
			tuple.put("kinds", choices);
			tuple.put("strategyId", this.tactic.getStrategy().getId());
		}
	}
}
