
package acme.features.fundraiser.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.entities.strategy.Strategy;
import acme.realms.Fundraiser;

@Service
public class FundraiserStrategyUpdateService extends AbstractService<Fundraiser, Strategy> {

	@Autowired
	protected FundraiserStrategyRepository	repository;
	protected Strategy						strategy;


	@Override
	public void load() {
		int id = this.getRequest().getData("id", int.class);
		this.strategy = this.repository.findOneStrategyById(id);
	}

	@Override
	public void authorise() {
		boolean result = false;
		final boolean isAuthenticated = super.getRequest().getPrincipal() != null;
		final boolean isFundraiser = isAuthenticated && super.getRequest().getPrincipal().hasRealmOfType(Fundraiser.class);

		if (isFundraiser && this.strategy != null) {
			final int activeFundraiserId = super.getRequest().getPrincipal().getActiveRealm().getId();
			final int ownerId = this.strategy.getFundraiser().getId();

			final boolean isOwner = activeFundraiserId == ownerId;
			final boolean isDraft = this.strategy.getDraftMode();

			result = isOwner && isDraft;
		}

		super.setAuthorised(result);
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
		this.repository.save(this.strategy);
	}

	@Override
	public void unbind() {
		super.unbindObject(this.strategy, "id", "ticker", "name", "description", "startMoment", "endMoment", "moreInfo", "draftMode", "expectedPercentage", "monthsActive");
	}
}
