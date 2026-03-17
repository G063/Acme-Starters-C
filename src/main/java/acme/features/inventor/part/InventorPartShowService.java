
package acme.features.inventor.part;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractService;
import acme.entities.invention.Part;
import acme.entities.invention.PartKind;
import acme.realms.Inventor;

@Service
public class InventorPartShowService extends AbstractService<Inventor, Part> {

	@Autowired
	protected InventorPartRepository	repository;
	protected Part						part;


	@Override
	public void load() {
		int id;
		id = super.getRequest().getData("id", int.class);
		this.part = this.repository.findPartById(id);
	}

	@Override
	public void authorise() {
		boolean isInventor = super.getRequest().getPrincipal().hasRealmOfType(Inventor.class);
		boolean isOwner = false;
		int currentInventorId = super.getRequest().getPrincipal().getActiveRealm().getId();

		if (this.part != null) {
			int ownerId = this.part.getInvention().getInventor().getId();
			isOwner = currentInventorId == ownerId;
		}
		super.setAuthorised(isInventor && isOwner && this.part.getInvention().getDraftMode());
	}

	@Override
	public void unbind() {
		super.unbindObject(this.part, "name", "description", "cost", "kind");
		this.getResponse().addGlobal("inventionId", this.part.getInvention().getId());
		this.getResponse().addGlobal("draftMode", this.part.getInvention().getDraftMode());
		this.getResponse().addGlobal("kind", this.part.getKind());

		final SelectChoices choices = SelectChoices.from(PartKind.class, this.part.getKind());
		this.getResponse().addGlobal("kinds", choices);
	}
}
