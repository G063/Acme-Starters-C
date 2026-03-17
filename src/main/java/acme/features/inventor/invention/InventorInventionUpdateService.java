
package acme.features.inventor.invention;

import java.util.Date;

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
		int id = this.getRequest().getData("id", int.class);
		this.invention = this.repository.findOneInventionById(id);
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
		super.bindObject(this.invention, "ticker", "name", "description", "moreInfo");
		Date start = this.getRequest().getData("startMoment", Date.class);
		Date end = this.getRequest().getData("endMoment", Date.class);
		this.invention.setStartMoment(start);
		this.invention.setEndMoment(end);
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
