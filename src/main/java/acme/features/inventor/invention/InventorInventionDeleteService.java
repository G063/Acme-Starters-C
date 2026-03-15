
package acme.features.inventor.invention;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.entities.invention.Invention;
import acme.entities.invention.Part;
import acme.realms.Inventor;

@Service
public class InventorInventionDeleteService extends AbstractService<Inventor, Invention> {

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
		boolean isOwner = this.invention != null && this.invention.getInventor().getId() == inventorId;
		boolean isDraft = this.invention != null && this.invention.getDraftMode();

		super.setAuthorised(isOwner && isDraft && status);
	}

	@Override
	public void bind() {
		super.bindObject(this.invention, "id");
	}

	@Override
	public void unbind() {
		super.getResponse().addGlobal("confirmation", "inventor.invention.delete.success");
	}

	@Override
	public void validate() {
		super.state(this.invention != null, "*", "inventor.invention.error.not-found");
	}
	@Override
	public void execute() {
		Collection<Part> parts = this.repository.findPartsByInventionId(this.invention.getId());
		this.repository.deleteAll(parts);
		this.repository.delete(this.invention);
	}
}
