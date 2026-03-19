package acme.features.auditor.auditSection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractService;
import acme.datatypes.SectionKind;
import acme.entities.audits.AuditSection;
import acme.realms.Auditor;

@Service
public class AuditorAuditSectionShowService extends AbstractService<Auditor, AuditSection> {

	@Autowired
	private AuditorAuditSectionRepository repository;

	private AuditSection auditSection;

	@Override
	public void load() {
		int id;

		id = super.getRequest().getData("id", int.class);
		this.auditSection = this.repository.findAuditSectionById(id);
	}

	@Override
	public void authorise() {
		boolean status;
		int auditorId;

		auditorId = super.getRequest().getPrincipal().getActiveRealm().getId();
		status = super.getRequest().getPrincipal().hasRealmOfType(Auditor.class) && this.auditSection != null && this.auditSection.getAuditReport().getAuditor().getId() == auditorId;

		super.setAuthorised(status);
	}

	@Override
	public void unbind() {
		Tuple tuple;

		tuple = super.unbindObject(this.auditSection, "name", "notes", "hours", "kind");
		tuple.put("kinds", SelectChoices.from(SectionKind.class, this.auditSection.getKind()));
		tuple.put("auditReportId", this.auditSection.getAuditReport().getId());
		tuple.put("auditReportDraftMode", Boolean.TRUE.equals(this.auditSection.getAuditReport().getDraftMode()));
	}

}
