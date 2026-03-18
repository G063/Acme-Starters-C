
package acme.features.any.invention;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.principals.Any;
import acme.client.services.AbstractService;
import acme.entities.invention.Invention;

@Service
public class AnyInventionShowService extends AbstractService<Any, Invention> {

	@Autowired
	protected AnyInventionRepository	repository;
	protected Invention					invention;


	@Override
	public void load() {
		Integer id = super.getRequest().getData("id", Integer.class);

		if (id != null)
			this.invention = this.repository.findInventionById(id);
		else
			this.invention = null;
	}

	@Override
	public void authorise() {
		boolean result = false;
		if (this.invention != null)
			result = !this.invention.getDraftMode();

		super.setAuthorised(result);
	}

	@Override
	public void unbind() {
		super.unbindObject(this.invention, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo");
		super.unbindGlobal("monthsActive", this.invention.getMonthsActive());
		super.unbindGlobal("cost", this.invention.getCost());
		super.getResponse().addGlobal("inventorId", this.invention.getInventor().getId());
	}
}
