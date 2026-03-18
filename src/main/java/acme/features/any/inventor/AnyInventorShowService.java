
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
	private Integer					inventionId;


	@Override
	public void load() {
		Integer inventorId = super.getRequest().getData("id", Integer.class);
		Integer invId = super.getRequest().getData("inventionId", Integer.class);
		if (inventorId != null)
			this.inventor = this.repository.findInventorById(inventorId);
		else
			this.inventor = null;

		this.inventionId = invId;
	}

	@Override
	public void authorise() {
		if (this.inventor == null || this.inventionId == null) {
			super.setAuthorised(false);
			return;
		}
		int id = this.inventor.getId();
		Long count = this.repository.countPublishedInventionByInventor(this.inventionId, id);

		super.setAuthorised(count != null && count > 0);
	}

	@Override
	public void unbind() {
		Tuple tuple;
		tuple = super.unbindObject(this.inventor, "userAccount.identity.name", "userAccount.identity.surname", "userAccount.identity.email", "bio", "keyWords", "licensed");
		tuple.put("inventionId", this.inventionId);
	}
}
