
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
		int id;

		id = super.getRequest().getData("id", int.class);
		this.invention = this.repository.findInventionById(id);
	}

	@Override
	public void authorise() {
		super.setAuthorised(this.invention != null && !this.invention.getDraftMode());
	}

	@Override
	public void unbind() {
		super.unbindObject(this.invention, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo");
		super.unbindGlobal("monthsActive", this.invention.getMonthsActive());
		super.unbindGlobal("cost", this.invention.getCost());
		super.getResponse().addGlobal("inventorId", this.invention.getInventor().getId());
	}
}
