
package acme.features.inventor.invention;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.services.AbstractService;
import acme.entities.invention.Invention;
import acme.realms.Inventor;

@Service
public class InventorInventionShowService extends AbstractService<Inventor, Invention> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected InventorInventionRepository	repository;

	protected Invention						invention;

	// AbstractService interface ----------------------------------------------


	@Override
	public void load() {
		int id;

		// 1. Obtenemos el objeto (evitamos errores de casting de tipos)
		Object idObject = super.getRequest().getData().get("id");

		// 2. Conversión segura a int
		id = Integer.parseInt(idObject.toString());

		// 3. Carga de la entidad
		this.invention = this.repository.findOneInventionById(id);

		// NOTA: No hacemos addData(invention) aquí para evitar el AssertionError
	}

	@Override
	public void authorise() {
		boolean result;
		int activeInventorId;

		if (this.getRequest().getPrincipal() == null || !this.getRequest().getPrincipal().hasRealmOfType(Inventor.class))
			result = false;
		else {
			activeInventorId = this.getRequest().getPrincipal().getActiveRealm().getId();
			// Solo el dueño puede verla (da igual si es draft o no para el inventor)
			result = this.invention != null && this.invention.getInventor().getId() == activeInventorId;
		}

		super.setAuthorised(result);
	}

	@Override
	public void unbind() {
		Tuple tuple;

		// 1. Desvinculamos los atributos básicos (EXCLUIMOS "cost" de aquí para controlarlo nosotros)
		tuple = super.unbindObject(this.invention, "id", "ticker", "name", "description", "draftMode", "startMoment", "endMoment", "moreInfo");
		tuple.put("cost", this.invention.getCost());

		// 4. Añadimos otros datos calculados
		tuple.put("monthsActive", this.invention.getMonthsActive());
	}
}
