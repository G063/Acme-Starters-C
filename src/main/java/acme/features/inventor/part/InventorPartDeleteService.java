
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
		Integer id = this.getRequest().getData("id", Integer.class);

		if (id != null)
			this.part = this.repository.findPartById(id);
		else
			this.part = null;
	}

	@Override
	public void authorise() {
		boolean isInventor = this.getRequest().getPrincipal().hasRealmOfType(Inventor.class);
		boolean isOwner = false;
		boolean isDraft = false;
		if (this.part != null) {
			int inventorId = this.getRequest().getPrincipal().getActiveRealm().getId();
			isOwner = this.part.getInvention().getInventor().getId() == inventorId;
			isDraft = this.part.getInvention().getDraftMode();
		}
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
		this.repository.delete(this.part);
	}
}
