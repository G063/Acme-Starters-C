
package acme.features.fundraiser.tactic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	public void authorise() {
		boolean status;
		status = super.getRequest().getPrincipal().hasRealmOfType(Fundraiser.class);

		final int id = super.getRequest().getData("id", int.class);
		this.tactic = this.repository.findOneTacticById(id);

		boolean result = false;
		if (this.tactic != null) {
			final int fundraiserId = super.getRequest().getPrincipal().getActiveRealm().getId();
			final boolean isOwner = this.tactic.getStrategy().getFundraiser().getId() == fundraiserId;
			final boolean isDraft = this.tactic.getStrategy().getDraftMode();
			result = isOwner && isDraft && status;
		}
		super.setAuthorised(result);
	}

	@Override
	public void load() {
		final int id = super.getRequest().getData("id", int.class);
		this.tactic = this.repository.findOneTacticById(id);
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
	public void unbind() {
		super.unbindObject(this.tactic, "id", "name", "notes", "expectedPercentage", "kind");
		super.getResponse().addGlobal("strategyId", this.tactic.getStrategy().getId());
	}

	@Override
	public void execute() {
		this.repository.save(this.tactic);
	}
}
