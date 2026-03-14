
package acme.features.inventor.part;

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
		// 1. Es inventor
		boolean isInventor = this.getRequest().getPrincipal().hasRealmOfType(Inventor.class);

		// 2. Seguridad: ¿Es el dueño de la invención?
		final String inventionIdStr = this.getRequest().getData("inventionId", String.class);
		boolean isOwner = false;
		if (inventionIdStr != null && !inventionIdStr.isBlank()) {
			int invId = Integer.parseInt(inventionIdStr);
			Invention inv = this.repository.findOneInventionById(invId);
			int activeInventorId = this.getRequest().getPrincipal().getActiveRealm().getId();
			isOwner = inv != null && inv.getInventor().getId() == activeInventorId;
		}

		super.setAuthorised(isInventor && isOwner);
	}

	@Override
	public void load() {
		System.out.println(">>> LOAD: Iniciando carga de la pieza");
		this.part = this.newObject(Part.class);

		final String inventionIdStr = this.getRequest().getData("inventionId", String.class);
		System.out.println(">>> LOAD: inventionId recibido = " + inventionIdStr);

		if (inventionIdStr != null && !inventionIdStr.isBlank()) {
			final int inventionId = Integer.parseInt(inventionIdStr);
			final Invention invention = this.repository.findOneInventionById(inventionId);
			this.part.setInvention(invention);
			System.out.println(">>> LOAD: Invención encontrada y asignada: " + (invention != null));
		}
	}

	@Override
	public void bind() {
		System.out.println(">>> BIND: Iniciando bindObject");
		super.bindObject(this.part, "name", "description", "cost", "kind");

		if (this.part.getInvention() == null) {
			System.out.println(">>> BIND: La invención era null tras el bind, intentando recuperar...");
			final String reqId = this.getRequest().getData("inventionId", String.class);
			if (reqId != null && !reqId.isBlank()) {
				final int inventionId = Integer.parseInt(reqId);
				final Invention invention = this.repository.findOneInventionById(inventionId);
				this.part.setInvention(invention);
				System.out.println(">>> BIND: Invención recuperada manualmente: " + (invention != null));
			}
		}

		if (this.part.getCost() != null)
			System.out.println(">>> BIND: Coste bindeado = " + this.part.getCost().getAmount() + " " + this.part.getCost().getCurrency());
	}

	@Override
	public void validate() {
		System.out.println(">>> VALIDATE: Iniciando validación de la pieza");

		// Esto disparará el PartValidator y el MoneyValidator
		super.validateObject(this.part);

		System.out.println(">>> VALIDATE: ¿Hay errores tras validateObject? " + super.getErrors().hasErrors());

		super.state(this.part.getInvention() != null, "*", "inventor.part.error.no-invention");

		if (super.getErrors().hasErrors())
			System.out.println(">>> VALIDATE: Errores detectados: " + super.getErrors().toString());
		else
			System.out.println(">>> VALIDATE: Todo OK, procediendo a execute");
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
		System.out.println(">>> EXECUTE: Guardando pieza en el repositorio...");
		try {
			this.repository.save(this.part);
			System.out.println(">>> EXECUTE: ¡Guardado exitoso!");

			final int inventionId = this.part.getInvention().getId();
			super.getResponse().setView("redirect:list?inventionId=" + inventionId);
		} catch (Exception e) {
			System.out.println(">>> EXECUTE: ERROR FATAL al guardar: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
