
package acme.features.inventor.part;

import java.util.ArrayList;
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
		Integer inventionId = this.getRequest().getData("inventionId", Integer.class);

		if (inventionId != null) {
			this.parts = this.repository.findPartsByInventionId(inventionId);
			this.getResponse().addGlobal("inventionId", inventionId);
		} else
			this.parts = new ArrayList<>();
	}

	@Override
	public void unbind() {
		super.unbindObjects(this.parts, "name", "description", "cost", "kind");
	}

	@Override
	public void authorise() {
		Integer inventionId = this.getRequest().getData("inventionId", Integer.class);
		boolean result = false;

		if (inventionId != null) {
			Invention inv = this.repository.findOneInventionById(inventionId);

			if (inv != null) {
				int activeInventorId = super.getRequest().getPrincipal().getActiveRealm().getId();
				boolean isOwner = inv.getInventor().getId() == activeInventorId;
				boolean isDraft = inv.getDraftMode();

				result = isOwner && isDraft;
			}
		}
		super.setAuthorised(result);
	}

}
