package acme.features.auditor.auditSection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractService;
import acme.datatypes.SectionKind;
import acme.entities.audits.AuditReport;
import acme.entities.audits.AuditSection;
import acme.realms.Auditor;

@Service
public class AuditorAuditSectionCreateService extends AbstractService<Auditor, AuditSection> {

	@Autowired
	private AuditorAuditSectionRepository repository;

	private AuditSection auditSection;

	@Override
	public void load() {
		int auditReportId;
		AuditReport auditReport;

		auditReportId = super.getRequest().getData("auditReportId", int.class);
		auditReport = this.repository.findAuditReportById(auditReportId);

		this.auditSection = this.newObject(AuditSection.class);
		this.auditSection.setAuditReport(auditReport);
	}

	@Override
	public void authorise() {
		boolean status;
		int auditorId;
		AuditReport auditReport;

		auditorId = super.getRequest().getPrincipal().getActiveRealm().getId();
		auditReport = this.auditSection == null ? null : this.auditSection.getAuditReport();
		status = super.getRequest().getPrincipal().hasRealmOfType(Auditor.class) && auditReport != null && auditReport.getAuditor().getId() == auditorId && Boolean.TRUE.equals(auditReport.getDraftMode());

		super.setAuthorised(status);
	}

	@Override
	public void bind() {
		super.bindObject(this.auditSection, "name", "notes", "hours", "kind");
	}

	@Override
	public void validate() {
		super.validateObject(this.auditSection);
	}

	@Override
	public void execute() {
		this.repository.save(this.auditSection);
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
