
package acme.features.fundraiser.strategy;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.helpers.MessageHelper;
import acme.client.services.AbstractService;
import acme.entities.strategy.Strategy;
import acme.realms.Fundraiser;

@Service
public class FundraiserStrategyListService extends AbstractService<Fundraiser, Strategy> {

	@Autowired
	protected FundraiserStrategyRepository	repository;
	protected Collection<Strategy>			strategies;


	@Override
	public void load() {
		if (this.getRequest().getPrincipal() != null && this.getRequest().getPrincipal().hasRealmOfType(Fundraiser.class)) {
			int fundraiserId = this.getRequest().getPrincipal().getActiveRealm().getId();
			this.strategies = this.repository.findStrategiesByFundraiserId(fundraiserId);
		} else
			this.strategies = new ArrayList<>();
	}

	@Override
	public void authorise() {
		final boolean hasPrincipal = super.getRequest().getPrincipal() != null;
		super.setAuthorised(hasPrincipal && this.getRequest().getPrincipal().hasRealmOfType(Fundraiser.class));
	}

	@Override
	public void unbind() {
		for (Strategy strategy : this.strategies) {
			Tuple tuple;

			tuple = super.unbindObject(strategy, "ticker", "name");

			tuple.put("draftMode", this.getDraftModeLabel(strategy.getDraftMode()));
		}
	}

	private String getDraftModeLabel(final Boolean draftMode) {
		String key;

		key = Boolean.TRUE.equals(draftMode) ? "fundraiser.strategy.list.draft.true" : "fundraiser.strategy.list.draft.false";

		return MessageHelper.getMessage(key);
	}
}
