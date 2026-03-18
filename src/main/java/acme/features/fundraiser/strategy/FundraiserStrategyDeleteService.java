
package acme.features.fundraiser.strategy;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.entities.strategy.Strategy;
import acme.entities.strategy.Tactic;
import acme.realms.Fundraiser;

@Service
public class FundraiserStrategyDeleteService extends AbstractService<Fundraiser, Strategy> {

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
		boolean isDraft = false;

		if (status && this.strategy != null) {
			int fundraiserId = this.getRequest().getPrincipal().getActiveRealm().getId();
			isOwner = this.strategy.getFundraiser().getId() == fundraiserId;
			isDraft = this.strategy.getDraftMode();
		}

		super.setAuthorised(isOwner && isDraft && status);
	}

	@Override
	public void bind() {
		super.bindObject(this.strategy, "id");
	}

	@Override
	public void validate() {
		super.state(this.strategy != null, "*", "fundraiser.strategy.error.not-found");
	}

	@Override
	public void execute() {
		Collection<Tactic> tactics = this.repository.findTacticsByStrategyId(this.strategy.getId());
		this.repository.deleteAll(tactics);

		this.repository.delete(this.strategy);
	}

	@Override
	public void unbind() {
		super.getResponse().addGlobal("confirmation", "fundraiser.strategy.delete.success");
	}
}
