
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
		Integer id = this.getRequest().getData("id", Integer.class);

		if (id != null)
			this.invention = this.repository.findOneInventionById(id);
		else
			this.invention = null;
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

	@Override
	public void bind() {
		super.bindObject(this.invention, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo");
	}
}
