
package acme.features.inventor.invention;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.entities.invention.Invention;
import acme.realms.Inventor;

@Service
public class InventorInventionUpdateService extends AbstractService<Inventor, Invention> {

	@Autowired
	protected InventorInventionRepository	repository;

	protected Invention						invention;


	@Override
	public void load() {
		int id = this.getRequest().getData("id", int.class);
		this.invention = this.repository.findOneInventionById(id);

		// BORRA ESTA LÍNEA:
		// this.getResponse().addData(this.invention); 
	}

	@Override
	public void authorise() {
		int inventorId = this.getRequest().getPrincipal().getActiveRealm().getId();
		boolean isOwner = this.invention != null && this.invention.getInventor().getId() == inventorId;

		// REQUISITO S1/4: Solo se puede actualizar si está en DRAFT MODE
		boolean isDraft = this.invention != null && this.invention.getDraftMode();

		super.setAuthorised(isOwner && isDraft);
	}

	@Override
	public void bind() {
		// Permitimos editar los campos básicos
		super.bindObject(this.invention, "ticker", "name", "description", "moreInfo");

		// Para las fechas usamos los setters de String que creaste en la entidad
		Date start = this.getRequest().getData("startMoment", Date.class);
		Date end = this.getRequest().getData("endMoment", Date.class);
		this.invention.setStartMoment(start);
		this.invention.setEndMoment(end);
	}

	@Override
	public void unbind() {
		// Es CRÍTICO añadir "id" aquí para que aparezca el botón de borrado
		super.unbindObject(this.invention, "id", "ticker", "name", "description", "startMoment", "endMoment", "moreInfo", "draftMode");
	}

	@Override
	public void validate() {
		super.validateObject(this.invention);

		acme.client.components.models.Errors errors = this.getResponse().getErrors();

		// Validar Ticker único (excluyendo la propia invención que estamos editando)
		if (this.invention.getTicker() != null && !errors.hasErrors("ticker")) {
			Invention existing = this.repository.findInventionByTicker(this.invention.getTicker());
			super.state(existing == null || existing.getId() == this.invention.getId(), "ticker", "inventor.invention.form.error.ticker-duplicated");
		}

		// Validar secuencia de fechas
		if (this.invention.getStartMoment() != null && this.invention.getEndMoment() != null) {
			boolean sequence = this.invention.getEndMoment().after(this.invention.getStartMoment());
			super.state(sequence, "endMoment", "inventor.invention.form.error.invalid-sequence");
		}
	}

	@Override
	public void execute() {
		this.repository.save(this.invention);
	}
}
