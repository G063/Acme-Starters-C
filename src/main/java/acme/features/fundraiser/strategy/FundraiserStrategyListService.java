
package acme.features.fundraiser.strategy;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.entities.strategy.Strategy;
import acme.realms.Fundraiser;

@Service
public class FundraiserStrategyListService extends AbstractService<Fundraiser, Strategy> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected FundraiserStrategyRepository	repository;
	protected Collection<Strategy>			strategies;

	// AbstractService interface ----------------------------------------------


	@Override
	public void load() {
		int fundraiserId;
		fundraiserId = this.getRequest().getPrincipal().getActiveRealm().getId();
		this.strategies = this.repository.findStrategiesByFundraiserId(fundraiserId);
	}

	@Override
	public void authorise() {
		super.setAuthorised(this.getRequest().getPrincipal().hasRealmOfType(Fundraiser.class));
	}

	@Override
	public void unbind() {
		super.unbindObjects(this.strategies, "ticker", "name", "draftMode");
	}
}
