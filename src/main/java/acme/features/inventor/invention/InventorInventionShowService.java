
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

		id = super.getRequest().getData("id", int.class);
		this.invention = this.repository.findOneInventionById(id);
	}

	@Override
	public void authorise() {
		boolean result;
		int activeInventorId;

		if (this.getRequest().getPrincipal() == null || !this.getRequest().getPrincipal().hasRealmOfType(Inventor.class))
			result = false;
		else {
			activeInventorId = this.getRequest().getPrincipal().getActiveRealm().getId();
			result = this.invention != null && this.invention.getInventor().getId() == activeInventorId;
		}

		super.setAuthorised(result);
	}

	@Override
	public void unbind() {
		Tuple tuple;
		tuple = super.unbindObject(this.invention, "id", "ticker", "name", "description", "draftMode", "startMoment", "endMoment", "moreInfo");
		tuple.put("cost", this.invention.getCost());
		tuple.put("monthsActive", this.invention.getMonthsActive());
	}
}
