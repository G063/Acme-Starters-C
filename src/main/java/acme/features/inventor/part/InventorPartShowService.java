
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

		Object idObject = super.getRequest().getData().get("id");
		id = Integer.parseInt(idObject.toString());

		this.part = this.repository.findPartById(id);
	}

	@Override
	public void authorise() {
		boolean isInventor = super.getRequest().getPrincipal().hasRealmOfType(Inventor.class);

		super.setAuthorised(isInventor);
	}

	@Override
	public void unbind() {
		super.unbindObject(this.part, "name", "description", "cost", "kind");

		// Variables locales para el JSP
		this.getResponse().addGlobal("acme_id", this.part.getId());
		this.getResponse().addGlobal("inventionId", this.part.getInvention().getId());
		this.getResponse().addGlobal("draftMode", this.part.getInvention().getDraftMode());
		this.getResponse().addGlobal("kind", this.part.getKind());

		final SelectChoices choices = SelectChoices.from(PartKind.class, this.part.getKind());
		this.getResponse().addGlobal("kinds", choices);
	}
}
