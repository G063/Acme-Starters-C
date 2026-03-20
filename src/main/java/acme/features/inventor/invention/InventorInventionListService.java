
package acme.features.inventor.invention;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.helpers.MessageHelper;
import acme.client.services.AbstractService;
import acme.entities.invention.Invention;
import acme.realms.Inventor;

@Service
public class InventorInventionListService extends AbstractService<Inventor, Invention> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected InventorInventionRepository	repository;

	protected Collection<Invention>			inventions;

	// AbstractService interface ----------------------------------------------


	@Override
	public void load() {
		int inventorId;

		inventorId = this.getRequest().getPrincipal().getActiveRealm().getId();
		this.inventions = this.repository.findInventionsByInventorId(inventorId);
	}

	@Override
	public void authorise() {
		super.setAuthorised(this.getRequest().getPrincipal().hasRealmOfType(Inventor.class));
	}

	@Override
	public void unbind() {
		for (Invention s : this.inventions) {
			Tuple tuple;

			tuple = this.unbindObject(s, "ticker", "name", "cost");
			tuple.put("draftMode", this.getDraftModeLabel(s.getDraftMode()));
		}

	}

	private String getDraftModeLabel(final Boolean draftMode) {
		String key;

		key = Boolean.TRUE.equals(draftMode) ? "inventor.invention.form.value.draft" : "inventor.invention.form.value.published";

		return MessageHelper.getMessage(key);
	}
}
