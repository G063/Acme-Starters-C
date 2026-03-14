
package acme.features.authenticated.inventor.part;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractService;
import acme.entities.invention.Invention;
import acme.entities.invention.Part;
import acme.entities.invention.PartKind;
import acme.realms.Inventor;

@Service
public class InventorPartCreateService extends AbstractService<Inventor, Part> {

	@Autowired
	protected InventorPartRepository	repository;
	protected Part						part;


	@Override
	public void authorise() {
		super.setAuthorised(this.getRequest().getPrincipal().hasRealmOfType(Inventor.class));
	}

	@Override
	public void load() {
		this.part = this.newObject(Part.class);

		final String inventionIdStr = this.getRequest().getData("inventionId", String.class);
		if (inventionIdStr != null && !inventionIdStr.isBlank()) {
			final int inventionId = Integer.parseInt(inventionIdStr);
			final Invention invention = this.repository.findOneInventionById(inventionId);
			this.part.setInvention(invention);
		}
	}

	@Override
	public void bind() {
		super.bindObject(this.part, "name", "description", "cost", "kind");
	}

	@Override
	public void validate() {
		super.state(this.part.getName() != null && !this.part.getName().isBlank(), "name", "javax.validation.constraints.NotBlank.message");
		super.state(this.part.getDescription() != null && !this.part.getDescription().isBlank(), "description", "javax.validation.constraints.NotBlank.message");
		super.state(this.part.getKind() != null, "kind", "javax.validation.constraints.NotNull.message");

		super.state(this.part.getInvention() != null, "*", "inventor.part.error.no-invention");

		if (this.part.getCost() != null) {
			super.state("EUR".equals(this.part.getCost().getCurrency()), "cost", "inventor.part.error.currency");
			super.state(this.part.getCost().getAmount() >= 0, "cost", "inventor.part.error.negative-amount");
		}
	}

	@Override
	public void unbind() {
		super.unbindObject(this.part, "name", "description", "cost", "kind");

		int inventionId = 0;
		if (this.part.getInvention() != null)
			inventionId = this.part.getInvention().getId();
		else {
			final String reqId = this.getRequest().getData("inventionId", String.class);
			if (reqId != null && !reqId.isBlank())
				inventionId = Integer.parseInt(reqId);
		}

		this.getResponse().addGlobal("inventionId", inventionId);

		final SelectChoices choices = SelectChoices.from(PartKind.class, this.part.getKind());
		this.getResponse().addGlobal("kinds", choices);
	}

	@Override
	public void execute() {
		this.repository.save(this.part);
	}
}
