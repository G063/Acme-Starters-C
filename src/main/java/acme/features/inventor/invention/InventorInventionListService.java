
package acme.features.inventor.invention;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.entities.invention.Invention;
import acme.realms.Inventor;

@Service
public class InventorInventionListService extends AbstractService<Inventor, Invention> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected InventorInventionRepository	repository;

	protected Collection<Invention>			inventions;

	// AbstractService interface ----------------------------------------------


	@Override
	public void load() {
		int inventorId;

		inventorId = this.getRequest().getPrincipal().getActiveRealm().getId();
		this.inventions = this.repository.findInventionsByInventorId(inventorId);
	}

	@Override
	public void authorise() {
		super.setAuthorised(this.getRequest().getPrincipal().hasRealmOfType(Inventor.class));
	}

	@Override
	public void unbind() {
		// Primero desvinculamos los datos normales
		super.unbindObjects(this.inventions, "ticker", "name", "cost", "draftMode");

		// Intentaremos usar el unbind genérico para pasar los enlaces como strings
		// Si el framework no te deja manipular la respuesta directamente, 
		// la opción más limpia es la siguiente:
	}
}
