
package acme.features.any.inventor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.components.principals.Any;
import acme.client.services.AbstractService;
import acme.realms.Inventor;

@Service
public class AnyInventorShowService extends AbstractService<Any, Inventor> {

	@Autowired
	private AnyInventorRepository	repository;

	private Inventor				inventor;
	private int						inventionId;


	@Override
	public void load() {
		int inventorId = super.getRequest().getData("id", int.class);
		this.inventionId = super.getRequest().getData("inventionId", int.class);
		this.inventor = this.repository.findInventorById(inventorId);
	}

	@Override
	public void authorise() {
		int inventorId = super.getRequest().getData("id", int.class);
		int invId = super.getRequest().getData("inventionId", int.class);

		Long count = this.repository.countPublishedInventionByInventor(invId, inventorId);
		boolean status = count != null && count > 0;

		super.setAuthorised(status);
	}

	@Override
	public void unbind() {
		Tuple tuple;
		tuple = super.unbindObject(this.inventor, "userAccount.identity.name", "userAccount.identity.surname", "userAccount.identity.email", "bio", "keyWords", "licensed");
		tuple.put("inventionId", this.inventionId);
	}
}
