package acme.features.authenticated.auditor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.principals.Authenticated;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractService;
import acme.realms.Auditor;

@Service
public class AuthenticatedAuditorUpdateService extends AbstractService<Authenticated, Auditor> {

	@Autowired
	private AuthenticatedAuditorRepository repository;

	private Auditor auditor;

	@Override
	public void load() {
		int userAccountId;

		userAccountId = super.getRequest().getPrincipal().getAccountId();
		this.auditor = this.repository.findAuditorByUserAccountId(userAccountId);
	}

	@Override
	public void authorise() {
		super.setAuthorised(super.getRequest().getPrincipal().hasRealmOfType(Auditor.class));
	}

	@Override
	public void bind() {
		super.bindObject(this.auditor, "firm", "highlights", "solicitor");
	}

	@Override
	public void validate() {
		super.validateObject(this.auditor);
	}

	@Override
	public void execute() {
		this.repository.save(this.auditor);
	}

	@Override
	public void unbind() {
		super.unbindObject(this.auditor, "firm", "highlights", "solicitor");
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}

}
