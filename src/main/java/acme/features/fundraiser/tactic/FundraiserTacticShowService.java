
package acme.features.fundraiser.tactic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractService;
import acme.entities.strategy.Tactic;
import acme.entities.strategy.TacticKind;
import acme.realms.Fundraiser;

@Service
public class FundraiserTacticShowService extends AbstractService<Fundraiser, Tactic> {

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

		super.setAuthorised(status && isOwner);
	}

	@Override
	public void unbind() {
		super.unbindObject(this.tactic, "name", "notes", "expectedPercentage", "kind");
		this.getResponse().addGlobal("strategyId", this.tactic.getStrategy().getId());
		this.getResponse().addGlobal("draftMode", this.tactic.getStrategy().getDraftMode());
		this.getResponse().addGlobal("kind", this.tactic.getKind());

		final SelectChoices choices = SelectChoices.from(TacticKind.class, this.tactic.getKind());
		this.getResponse().addGlobal("kinds", choices);
	}
}
