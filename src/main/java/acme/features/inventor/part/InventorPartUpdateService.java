
package acme.features.inventor.part;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractService;
import acme.entities.invention.Part;
import acme.entities.invention.PartKind;
import acme.realms.Inventor;

@Service
public class InventorPartUpdateService extends AbstractService<Inventor, Part> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected InventorPartRepository	repository;

	protected Part						part;

	// AbstractService interface -------------------------------------------


	@Override
	public void load() {
		Integer id = super.getRequest().getData("id", Integer.class);

		if (id != null)
			this.part = this.repository.findPartById(id);
		else
			this.part = null;
	}

	@Override
	public void authorise() {
		boolean status = false;

		if (this.part != null) {
			boolean isDraft = this.part.getInvention().getDraftMode();
			boolean isOwner = this.part.getInvention().getInventor().isPrincipal();

			status = isDraft && isOwner;
		}

		super.setAuthorised(status);
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
	public void execute() {
		this.repository.save(this.part);
	}

	@Override
	public void unbind() {
		Tuple tuple;
		SelectChoices choices;

		choices = SelectChoices.from(PartKind.class, this.part.getKind());
		tuple = super.unbindObject(this.part, "name", "description", "cost", "kind");

		tuple.put("kinds", choices);
		tuple.put("inventionId", this.part.getInvention().getId());
		tuple.put("draftMode", this.part.getInvention().getDraftMode());
	}
}
