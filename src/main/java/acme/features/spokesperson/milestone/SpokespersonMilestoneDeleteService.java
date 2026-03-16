package acme.features.spokesperson.milestone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.entities.campaign.Campaign;
import acme.entities.campaign.Milestone;
import acme.realms.Spokesperson;

@Service
public class SpokespersonMilestoneDeleteService extends AbstractService<Spokesperson, Milestone> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected SpokespersonMilestoneRepository repository;

	protected Milestone milestone;

	// AbstractService interface -------------------------------------------

	@Override
	public void load() {
		int id;
		id = super.getRequest().getData("id", int.class);
		this.milestone = this.repository.findMilestoneById(id);
	}

	@Override
	public void authorise() {
		boolean isSpokesperson;
		boolean isOwner;
		boolean isDraft;
		int spokespersonId;

		isSpokesperson = this.getRequest().getPrincipal().hasRealmOfType(Spokesperson.class);
		spokespersonId = this.getRequest().getPrincipal().getActiveRealm().getId();
		isOwner = this.milestone != null && this.milestone.getCampaign().getSpokesperson().getId() == spokespersonId;
		isDraft = this.milestone != null && this.milestone.getCampaign().getDraftMode();

		super.setAuthorised(isSpokesperson && isOwner && isDraft);
	}

	@Override
	public void bind() {
		super.bindObject(this.milestone, "title", "achievements", "effort", "kind");
	}

	@Override
	public void validate() {
		super.validateObject(this.milestone);
	}

	@Override
	public void unbind() {
		super.unbindObject(this.milestone, "title", "achievements", "effort", "kind");
	}

	@Override
	public void execute() {
		this.repository.delete(this.milestone);
	}
}
