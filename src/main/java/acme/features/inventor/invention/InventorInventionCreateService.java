
package acme.features.inventor.invention;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractService;
import acme.entities.invention.Invention;
import acme.realms.Inventor;

@Service
public class InventorInventionCreateService extends AbstractService<Inventor, Invention> {

	@Autowired
	protected InventorInventionRepository	repository;
	protected Invention						invention;


	@Override
	public void load() {
		int userAccountId;
		Inventor inventor;

		userAccountId = super.getRequest().getPrincipal().getAccountId();
		inventor = this.repository.findInventorByUserAccountId(userAccountId);

		// IMPORTANTE: Usar this.newObject, NUNCA "new Invention()"
		this.invention = this.newObject(Invention.class);
		this.invention.setDraftMode(true);
		this.invention.setInventor(inventor);
	}

	@Override
	public void authorise() {
		// Igual que en Sponsorship para evitar bloqueos
		super.setAuthorised(true);
	}

	@Override
	public void bind() {
		// Debes incluir los momentos (start/end) para que no den error de validación
		super.bindObject(this.invention, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo");
	}

	@Override
	public void validate() {
		// Validación automática por anotaciones
		super.validateObject(this.invention);
	}

	@Override
	public void execute() {
		this.invention.setDraftMode(true);
		this.repository.save(this.invention);
	}

	@Override
	public void unbind() {
		Tuple tuple;

		// Asegúrate de que los campos coinciden EXACTAMENTE con el JSP
		tuple = super.unbindObject(this.invention, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo", "draftMode");
		tuple.put("draftModes", this.getDraftModeChoices(this.invention.getDraftMode()));
	}

	private SelectChoices getDraftModeChoices(final Boolean draftMode) {
		SelectChoices choices;
		choices = this.newObject(SelectChoices.class);
		choices.add("true", "inventor.invention.form.value.draft", draftMode.equals(true));
		choices.add("false", "inventor.invention.form.value.published", draftMode.equals(false));
		return choices;
	}
}
