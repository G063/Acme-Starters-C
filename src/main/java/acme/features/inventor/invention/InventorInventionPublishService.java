
package acme.features.inventor.invention;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.entities.invention.Invention;
import acme.realms.Inventor;

@Service
public class InventorInventionPublishService extends AbstractService<Inventor, Invention> {

	@Autowired
	protected InventorInventionRepository	repository;

	protected Invention						invention;


	@Override
	public void load() {
		int id = this.getRequest().getData("id", int.class);
		this.invention = this.repository.findOneInventionById(id);
	}

	@Override
	public void authorise() {
		boolean status;

		status = super.getRequest().getPrincipal().hasRealmOfType(Inventor.class);
		int inventorId = this.getRequest().getPrincipal().getActiveRealm().getId();
		boolean isOwner = this.invention.getInventor().getId() == inventorId;
		super.setAuthorised(isOwner && this.invention.getDraftMode() && status);
	}

	@Override
	public void bind() {
		super.bindObject(this.invention);
	}

	@Override
	public void unbind() {
		super.unbindObject(this.invention, "ticker", "name", "description", "draftMode", "startMoment", "endMoment", "moreInfo");
	}

	@Override
	public void validate() {
		super.validateObject(this.invention);
		int partCount = this.repository.countPartsByInventionId(this.invention.getId());
		super.state(partCount > 0, "*", "inventor.invention.form.error.no-parts");
	}

	@Override
	public void execute() {
		this.invention.setDraftMode(false);
		this.repository.save(this.invention);
	}
}
