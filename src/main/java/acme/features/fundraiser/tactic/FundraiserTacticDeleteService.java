
package acme.features.fundraiser.tactic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.entities.strategy.Tactic;
import acme.realms.Fundraiser;

@Service
public class FundraiserTacticDeleteService extends AbstractService<Fundraiser, Tactic> {

	@Autowired
	protected FundraiserTacticRepository	repository;
	protected Tactic						tactic;


	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		this.tactic = this.repository.findOneTacticById(id);
	}

	@Override
	public void authorise() {
		boolean status = super.getRequest().getPrincipal().hasRealmOfType(Fundraiser.class);
		int fundraiserId = super.getRequest().getPrincipal().getActiveRealm().getId();
		boolean isOwner = this.tactic != null && this.tactic.getStrategy().getFundraiser().getId() == fundraiserId;
		boolean isDraft = this.tactic != null && this.tactic.getStrategy().getDraftMode();

		super.setAuthorised(status && isOwner && isDraft);
	}

	@Override
	public void bind() {
		super.bindObject(this.tactic, "id");
	}

	@Override
	public void validate() {
		super.state(this.tactic != null, "*", "fundraiser.tactic.error.not-found");
	}

	@Override
	public void execute() {
		this.repository.delete(this.tactic);
		super.getResponse().setView("redirect:/fundraiser/tactic/list?strategyId=" + this.tactic.getStrategy().getId());
	}

	@Override
	public void unbind() {
		super.getResponse().addGlobal("confirmation", "fundraiser.tactic.delete.success");
	}
}
