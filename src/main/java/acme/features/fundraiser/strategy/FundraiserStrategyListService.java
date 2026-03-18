
package acme.features.fundraiser.strategy;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
		super.unbindObjects(this.strategies, "ticker", "name", "draftMode");
	}
}
