
package acme.features.inventor.invention;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.entities.invention.Invention;
import acme.realms.Inventor;

@Service
public class InventorInventionUpdateService extends AbstractService<Inventor, Invention> {

	@Autowired
	protected InventorInventionRepository	repository;

	protected Invention						invention;


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
		boolean result = false;
		final boolean isAuthenticated = super.getRequest().getPrincipal() != null;
		final boolean isInventor = isAuthenticated && super.getRequest().getPrincipal().hasRealmOfType(Inventor.class);

		if (isInventor && this.invention != null) {
			final int activeInventorId = super.getRequest().getPrincipal().getActiveRealm().getId();
			final int ownerId = this.invention.getInventor().getId();
			final boolean isOwner = activeInventorId == ownerId;
			final boolean isDraft = this.invention.getDraftMode();

			result = isOwner && isDraft;
		}

		super.setAuthorised(result);
	}
	@Override
	public void bind() {
		super.bindObject(this.invention, "ticker", "name", "description", "moreInfo", "startMoment", "endMoment");
	}

	@Override
	public void unbind() {
		super.unbindObject(this.invention, "id", "ticker", "name", "description", "startMoment", "endMoment", "moreInfo", "draftMode", "cost", "monthsActive");
	}

	@Override
	public void validate() {
		super.validateObject(this.invention);
	}

	@Override
	public void execute() {
		this.repository.save(this.invention);
	}
}
