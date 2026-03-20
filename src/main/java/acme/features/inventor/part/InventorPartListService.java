
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
	protected Invention					invention;


	@Override
	public void load() {
		Integer inventionId = this.getRequest().getData("inventionId", Integer.class);

		if (inventionId != null) {
			this.parts = this.repository.findPartsByInventionId(inventionId);
			this.invention = this.repository.findOneInventionById(inventionId);
			this.getResponse().addGlobal("inventionId", inventionId);
		} else
			this.parts = new ArrayList<>();
	}

	@Override
	public void unbind() {
		super.unbindObjects(this.parts, "name", "description", "cost", "kind");
		boolean isDraft = this.invention != null && this.invention.getDraftMode();
		this.getResponse().addGlobal("draftMode", isDraft);
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

				result = isOwner;
			}
		}
		super.setAuthorised(result);
	}

}
