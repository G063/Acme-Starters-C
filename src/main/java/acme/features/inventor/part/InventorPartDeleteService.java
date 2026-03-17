
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
		int id = this.getRequest().getData("id", int.class);
		this.part = this.repository.findPartById(id);
	}

	@Override
	public void authorise() {
		boolean isInventor = this.getRequest().getPrincipal().hasRealmOfType(Inventor.class);
		int inventorId = this.getRequest().getPrincipal().getActiveRealm().getId();
		boolean isOwner = this.part != null && this.part.getInvention().getInventor().getId() == inventorId;
		boolean isDraft = this.part != null && this.part.getInvention().getDraftMode();

		super.setAuthorised(isInventor && isOwner && isDraft);
	}

	@Override
	public void bind() {
	}

	@Override
	public void validate() {
	}

	@Override
	public void unbind() {
		super.unbindObject(this.part, "name", "description", "cost", "kind");
	}

	@Override
	public void execute() {
		int inventionId = this.part.getInvention().getId();

		this.repository.delete(this.part);

		super.getResponse().setView("redirect:/authenticated/inventor/part/list?inventionId=" + inventionId);
	}
}
