
package acme.features.fundraiser.tactic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractService;
import acme.entities.strategy.Tactic;
import acme.entities.strategy.TacticKind;
import acme.realms.Fundraiser;

@Service
public class FundraiserTacticUpdateService extends AbstractService<Fundraiser, Tactic> {

	@Autowired
	protected FundraiserTacticRepository	repository;
	protected Tactic						tactic;


	@Override
	public void load() {
		final int id = super.getRequest().getData("id", int.class);
		this.tactic = this.repository.findOneTacticById(id);
	}

	@Override
	public void authorise() {
		final boolean hasPrincipal = super.getRequest().getPrincipal() != null;
		final boolean status = hasPrincipal && super.getRequest().getPrincipal().hasRealmOfType(Fundraiser.class);
		boolean result = false;

		if (status && this.tactic != null) {
			final int fundraiserId = super.getRequest().getPrincipal().getActiveRealm().getId();
			final boolean isOwner = this.tactic.getStrategy().getFundraiser().getId() == fundraiserId;
			final boolean isDraft = this.tactic.getStrategy().getDraftMode();
			result = isOwner && isDraft;
		}

		super.setAuthorised(result);
	}

	@Override
	public void bind() {
		super.bindObject(this.tactic, "name", "notes", "expectedPercentage");

		final String kindValue = super.getRequest().getData("kind", String.class);
		try {
			if (kindValue != null)
				this.tactic.setKind(TacticKind.valueOf(kindValue));
		} catch (final Exception e) {
			this.tactic.setKind(null);
		}
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
		if (this.tactic != null) {
			Tuple tuple;
			SelectChoices choices;
			choices = SelectChoices.from(TacticKind.class, this.tactic.getKind());
			tuple = super.unbindObject(this.tactic, "id", "name", "notes", "expectedPercentage", "kind");
			tuple.put("kinds", choices);
			tuple.put("strategyId", this.tactic.getStrategy().getId());
			tuple.put("strategyDraftMode", this.tactic.getStrategy().getDraftMode());
		}
	}
}
