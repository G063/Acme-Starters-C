
package acme.features.inventor.part;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.entities.invention.Part;
import acme.realms.Inventor;

@Service
public class InventorPartDeleteService extends AbstractService<Inventor, Part> {

	@Autowired
	protected InventorPartRepository	repository;

	protected Part						part;


	@Override
	public void load() {
		// Obtenemos el id de la pieza (Part)
		int id = this.getRequest().getData("id", int.class);
		this.part = this.repository.findPartById(id);
	}

	@Override
	public void authorise() {
		// 1. Debe ser un Inventor
		boolean isInventor = this.getRequest().getPrincipal().hasRealmOfType(Inventor.class);

		int inventorId = this.getRequest().getPrincipal().getActiveRealm().getId();
		boolean isOwner = this.part != null && this.part.getInvention().getInventor().getId() == inventorId;

		boolean isDraft = this.part != null && this.part.getInvention().getDraftMode();

		super.setAuthorised(isInventor && isOwner && isDraft);
	}

	@Override
	public void bind() {
		super.bindObject(this.part, "name", "description", "cost", "kind");
	}

	@Override
	public void validate() {
		super.validateObject(this.part);
	}

	@Override
	public void unbind() {
		super.unbindObject(this.part, "name", "description", "cost", "kind");
	}

	@Override
	public void execute() {
		this.repository.delete(this.part);

		super.getResponse().setView("redirect:/authenticated/inventor/part/list?inventionId=" + this.part.getInvention().getId());
	}
}
