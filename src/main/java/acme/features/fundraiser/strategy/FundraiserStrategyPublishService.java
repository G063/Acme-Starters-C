
package acme.features.fundraiser.strategy;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.entities.strategy.Strategy;
import acme.realms.Fundraiser;

@Service
public class FundraiserStrategyPublishService extends AbstractService<Fundraiser, Strategy> {

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
		final boolean hasPrincipal = super.getRequest().getPrincipal() != null;
		final boolean status = hasPrincipal && super.getRequest().getPrincipal().hasRealmOfType(Fundraiser.class);
		boolean isOwner = false;

		if (status && this.strategy != null) {
			int fundraiserId = this.getRequest().getPrincipal().getActiveRealm().getId();
			isOwner = this.strategy.getFundraiser().getId() == fundraiserId;
		}

		super.setAuthorised(status && isOwner && this.strategy != null && this.strategy.getDraftMode());
	}

	@Override
	public void bind() {
		super.bindObject(this.strategy, "id");
	}

	@Override
	public void validate() {
		Date base = MomentHelper.getBaseMoment();
		Date start = this.strategy.getStartMoment();
		super.state(MomentHelper.isAfter(start, base), "*", "fundraiser.strategy.form.error.date-incorrect");
		super.validateObject(this.strategy);

		int tacticsCount = this.repository.countTacticsByStrategyId(this.strategy.getId());
		super.state(tacticsCount > 0, "*", "fundraiser.strategy.form.error.no-tactics");
	}

	@Override
	public void execute() {
		this.strategy.setDraftMode(false);
		this.repository.save(this.strategy);
	}

	@Override
	public void unbind() {
		super.unbindObject(this.strategy, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo", "draftMode", "expectedPercentage", "monthsActive");
	}
}
