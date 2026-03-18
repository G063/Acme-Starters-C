
package acme.features.fundraiser.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.services.AbstractService;
import acme.entities.strategy.Strategy;
import acme.realms.Fundraiser;

@Service
public class FundraiserStrategyCreateService extends AbstractService<Fundraiser, Strategy> {

	@Autowired
	protected FundraiserStrategyRepository	repository;
	protected Strategy						strategy;


	@Override
	public void load() {
		this.strategy = this.newObject(Strategy.class);
		this.strategy.setDraftMode(true);

		if (super.getRequest().getPrincipal() != null) {
			int fundraiserId = super.getRequest().getPrincipal().getActiveRealm().getId();
			Fundraiser fundraiser = this.repository.findFundraiserById(fundraiserId);
			this.strategy.setFundraiser(fundraiser);
		}
	}

	@Override
	public void authorise() {
		final boolean hasPrincipal = super.getRequest().getPrincipal() != null;
		super.setAuthorised(hasPrincipal && super.getRequest().getPrincipal().hasRealmOfType(Fundraiser.class));
	}

	@Override
	public void bind() {
		super.bindObject(this.strategy, "ticker", "name", "description", "moreInfo", "startMoment", "endMoment");
	}

	@Override
	public void validate() {
		super.validateObject(this.strategy);
	}

	@Override
	public void execute() {
		this.strategy.setDraftMode(true);
		this.repository.save(this.strategy);
	}

	@Override
	public void unbind() {
		Tuple tuple = super.unbindObject(this.strategy, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo");
		tuple.put("draftMode", this.strategy.getDraftMode());
	}
}
