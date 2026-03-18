
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
		Integer id = super.getRequest().getData("id", Integer.class);

		if (id != null)
			this.part = this.repository.findPartById(id);
		else
			this.part = null;
	}

	@Override
	public void authorise() {
		Integer id = super.getRequest().getData("id", Integer.class);

		boolean result = false;
		if (this.part != null && id != null)
			result = !this.part.getInvention().getDraftMode();

		super.setAuthorised(result);
	}

	@Override
	public void unbind() {
		if (this.part != null)
			super.unbindObject(this.part, "id", "name", "description", "cost", "kind", "invention.ticker");
	}
}
