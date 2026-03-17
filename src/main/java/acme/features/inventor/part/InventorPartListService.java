
package acme.features.inventor.part;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.entities.invention.Invention;
import acme.entities.invention.Part;
import acme.realms.Inventor;

@Service
public class InventorPartListService extends AbstractService<Inventor, Part> {

	@Autowired
	protected InventorPartRepository	repository;

	protected Collection<Part>			parts;


	@Override
	public void load() {
		int inventionId = this.getRequest().getData("inventionId", int.class);

		this.parts = this.repository.findPartsByInventionId(inventionId);

		this.getResponse().addGlobal("inventionId", inventionId);
	}

	@Override
	public void unbind() {
		super.unbindObjects(this.parts, "name", "description", "cost", "kind");
	}

	@Override
	public void authorise() {
		boolean result = false;
		int inventionId = super.getRequest().getData("inventionId", int.class);
		Invention inv = this.repository.findOneInventionById(inventionId);

		if (inv != null) {
			int activeInventorId = super.getRequest().getPrincipal().getActiveRealm().getId();
			result = inv.getInventor().getId() == activeInventorId;
		}

		super.setAuthorised(result && inv.getDraftMode());
	}

}
