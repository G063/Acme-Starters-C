
package acme.features.inventor.invention;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.services.AbstractService;
import acme.entities.invention.Invention;
import acme.realms.Inventor;

@Service
public class InventorInventionCreateService extends AbstractService<Inventor, Invention> {

	@Autowired
	protected InventorInventionRepository	repository;
	protected Invention						invention;


	@Override
	public void load() {
		int userAccountId;
		Inventor inventor;

		userAccountId = super.getRequest().getPrincipal().getAccountId();
		inventor = this.repository.findInventorByUserAccountId(userAccountId);

		this.invention = this.newObject(Invention.class);
		this.invention.setDraftMode(true);
		this.invention.setInventor(inventor);
	}

	@Override
	public void authorise() {
		boolean status;

		status = super.getRequest().getPrincipal().hasRealmOfType(Inventor.class);
		super.setAuthorised(status);
	}

	@Override
	public void bind() {
		super.bindObject(this.invention, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo");
	}

	@Override
	public void validate() {
		super.validateObject(this.invention);
	}

	@Override
	public void execute() {
		this.invention.setDraftMode(true);
		this.repository.save(this.invention);
	}

	@Override
	public void unbind() {
		Tuple tuple;
		tuple = super.unbindObject(this.invention, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo");
		tuple.put("draftMode", this.invention.getDraftMode());
	}
}
