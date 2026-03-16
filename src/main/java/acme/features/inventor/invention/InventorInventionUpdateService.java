
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
		boolean status;

		status = super.getRequest().getPrincipal().hasRealmOfType(Inventor.class);
		int inventorId = this.getRequest().getPrincipal().getActiveRealm().getId();
		boolean isOwner = this.invention != null && this.invention.getInventor().getId() == inventorId;
		boolean isDraft = this.invention != null && this.invention.getDraftMode();

		super.setAuthorised(isOwner && isDraft && status);
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
		super.unbindObject(this.invention, "id", "ticker", "name", "description", "startMoment", "endMoment", "moreInfo", "draftMode");
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
