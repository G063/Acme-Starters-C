
package acme.features.any.part;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.principals.Any;
import acme.client.services.AbstractService;
import acme.entities.invention.Invention;
import acme.entities.invention.Part;

@Service
public class AnyPartListService extends AbstractService<Any, Part> {

	@Autowired
	protected AnyPartRepository	repository;
	protected Collection<Part>	parts;
	protected Invention			invention;


	@Override
	public void load() {
		Integer inventionId;
		inventionId = super.getRequest().getData("inventionId", Integer.class);
		this.invention = this.repository.findInventionById(inventionId);
		this.parts = this.repository.findPartsByInventionId(inventionId);
	}

	@Override
	public void authorise() {
		boolean status;

		status = this.invention != null && !this.invention.getDraftMode();

		super.setAuthorised(status);
	}

	@Override
	public void unbind() {
		super.unbindObjects(this.parts, "name", "description", "cost", "kind");
	}
}
