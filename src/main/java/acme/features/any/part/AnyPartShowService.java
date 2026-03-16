
package acme.features.any.part;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.principals.Any;
import acme.client.services.AbstractService;
import acme.entities.invention.Part;

@Service
public class AnyPartShowService extends AbstractService<Any, Part> {

	@Autowired
	protected AnyPartRepository	repository;
	protected Part				part;


	@Override
	public void load() {
		int id;
		id = super.getRequest().getData("id", int.class);
		this.part = this.repository.findPartById(id);
	}

	@Override
	public void authorise() {
		super.setAuthorised(this.part != null && this.part.getInvention().getDraftMode());
	}

	@Override
	public void unbind() {
		if (this.part != null)
			super.unbindObject(this.part, "id", "name", "description", "cost", "kind", "invention.ticker");
	}
}
