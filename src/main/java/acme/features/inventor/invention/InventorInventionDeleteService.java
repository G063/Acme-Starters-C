
package acme.features.inventor.invention;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.entities.invention.Invention;
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
		int inventorId = this.getRequest().getPrincipal().getActiveRealm().getId();
		boolean isOwner = this.invention != null && this.invention.getInventor().getId() == inventorId;

		// REQUISITO S1/4: Solo se puede borrar si está en DRAFT MODE
		boolean isDraft = this.invention != null && this.invention.getDraftMode();

		super.setAuthorised(isOwner && isDraft);
	}

	@Override
	public void bind() {
		// Al no bindear los campos de texto, ignoramos las fechas incorrectas.
		// Solo bindeamos el id para que el framework sepa que seguimos trabajando con la misma entidad.
		super.bindObject(this.invention, "id");
	}

	@Override
	public void unbind() {
		// No desvincules campos de texto, solo lo mínimo necesario
		// o déjalo vacío para que no intente procesar las fechas corruptas de nuevo
		super.getResponse().addGlobal("confirmation", "inventor.invention.delete.success");
	}

	@Override
	public void validate() {
		// No valides el objeto. Solo asegúrate de que existe
		super.state(this.invention != null, "*", "inventor.invention.error.not-found");
	}
	@Override
	public void execute() {
		this.repository.deletePartsByInventionId(this.invention.getId());
		this.repository.delete(this.invention);

		// El "redirect:" es obligatorio para que el navegador pida la URL de la lista
		// y se ejecute el ListService correspondiente.
		super.getResponse().setView("redirect:/inventor/invention/list");
	}
}
