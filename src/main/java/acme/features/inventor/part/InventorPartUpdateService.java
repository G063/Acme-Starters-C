
package acme.features.inventor.part;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractService;
import acme.entities.invention.Part;
import acme.entities.invention.PartKind;
import acme.realms.Inventor;

@Service
public class InventorPartUpdateService extends AbstractService<Inventor, Part> {

	@Autowired
	protected InventorPartRepository	repository;
	protected Part						part;


	@Override
	public void authorise() {
		final int id = super.getRequest().getData("id", int.class);
		this.part = this.repository.findPartById(id);

		boolean result = false;
		if (this.part != null) {
			final int inventorId = super.getRequest().getPrincipal().getActiveRealm().getId();
			final boolean isOwner = this.part.getInvention().getInventor().getId() == inventorId;
			final boolean isDraft = this.part.getInvention().getDraftMode();
			result = isOwner && isDraft;
		}
		super.setAuthorised(result);
	}

	@Override
	public void load() {
		final int id = super.getRequest().getData("id", int.class);
		this.part = this.repository.findPartById(id);
	}

	@Override
	public void bind() {
		super.bindObject(this.part, "name", "description", "cost");

		final String kindValue = super.getRequest().getData("kind", String.class);
		try {
			if (kindValue != null)
				this.part.setKind(PartKind.valueOf(kindValue));
		} catch (final Exception e) {
			this.part.setKind(null);
		}
	}

	@Override
	public void validate() {
		super.validateObject(this.part);
	}

	@Override
	public void unbind() {
		super.unbindObject(this.part, "name", "description", "cost", "kind");

		super.getResponse().addGlobal("acme_id", this.part.getId());
		super.getResponse().addGlobal("draftMode", this.part.getInvention().getDraftMode());
		super.getResponse().addGlobal("inventionId", this.part.getInvention().getId());

		final SelectChoices choices = SelectChoices.from(PartKind.class, this.part.getKind());
		super.getResponse().addGlobal("kinds", choices);
	}

	@Override
	public void execute() {
		this.repository.save(this.part);

		final int inventionId = this.part.getInvention().getId();
		super.getResponse().setView("redirect:list?inventionId=" + inventionId);
	}
}
