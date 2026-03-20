package acme.features.auditor.auditReport;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.entities.audits.AuditReport;
import acme.entities.audits.AuditSection;
import acme.realms.Auditor;

@Service
public class AuditorAuditReportDeleteService extends AbstractService<Auditor, AuditReport> {

	@Autowired
	private AuditorAuditReportRepository repository;

	private AuditReport auditReport;

	@Override
	public void load() {
		int id;

		id = super.getRequest().getData("id", int.class);
		this.auditReport = this.repository.findAuditReportById(id);
	}

	@Override
	public void authorise() {
		int auditorId;
		boolean isOwner;
		boolean isDraft;

		auditorId = super.getRequest().getPrincipal().getActiveRealm().getId();
		isOwner = this.auditReport != null && this.auditReport.getAuditor().getId() == auditorId;
		isDraft = this.auditReport != null && this.auditReport.getDraftMode();

		super.setAuthorised(super.getRequest().getPrincipal().hasRealmOfType(Auditor.class) && isOwner && isDraft);
	}

	@Override
	public void bind() {
		super.bindObject(this.auditReport, "id");
	}

	@Override
	public void validate() {
		super.state(this.auditReport != null, "*", "auditor.audit-report.error.not-found");
	}

	@Override
	public void execute() {
		Collection<AuditSection> auditSections;

		auditSections = this.repository.findAuditSectionsByAuditReportId(this.auditReport.getId());
		this.repository.deleteAll(auditSections);
		this.repository.delete(this.auditReport);

		super.getResponse().setView("redirect:/auditor/audit-report/list");
	}

	@Override
	public void unbind() {
		super.getResponse().addGlobal("confirmation", "auditor.audit-report.delete.success");
	}

}
