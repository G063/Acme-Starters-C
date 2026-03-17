
package acme.features.fundraiser.tactic;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.entities.strategy.Strategy;
import acme.entities.strategy.Tactic;
import acme.realms.Fundraiser;

@Service
public class FundraiserTacticListService extends AbstractService<Fundraiser, Tactic> {

	@Autowired
	protected FundraiserTacticRepository	repository;
	protected Strategy						strategy;
	protected Collection<Tactic>			tactics;


	@Override
	public void load() {
		int strategyId = super.getRequest().getData("strategyId", int.class);
		this.strategy = this.repository.findOneStrategyById(strategyId);
		this.tactics = this.repository.findManyTacticsByStrategyId(strategyId);
	}

	@Override
	public void authorise() {
		final boolean hasPrincipal = super.getRequest().getPrincipal() != null;
		final boolean status = hasPrincipal && super.getRequest().getPrincipal().hasRealmOfType(Fundraiser.class);
		boolean isOwner = false;

		if (status && this.strategy != null) {
			int fundraiserId = super.getRequest().getPrincipal().getActiveRealm().getId();
			isOwner = this.strategy.getFundraiser().getId() == fundraiserId;
		}

		super.setAuthorised(status && isOwner);
	}

	@Override
	public void unbind() {
		if (this.strategy != null) {
			super.getResponse().addGlobal("strategyId", this.strategy.getId());
			super.getResponse().addGlobal("strategyDraftMode", this.strategy.getDraftMode());
		}
		super.unbindObjects(this.tactics, "name", "expectedPercentage", "kind", "notes");
	}
}
