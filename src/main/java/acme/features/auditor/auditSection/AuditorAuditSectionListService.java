package acme.features.auditor.auditSection;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.entities.audits.AuditReport;
import acme.entities.audits.AuditSection;
import acme.realms.Auditor;

@Service
public class AuditorAuditSectionListService extends AbstractService<Auditor, AuditSection> {

	@Autowired
	private AuditorAuditSectionRepository repository;

	private Collection<AuditSection> auditSections;
	private AuditReport auditReport;

	@Override
	public void load() {
		int auditReportId;

		auditReportId = super.getRequest().getData("auditReportId", int.class);
		this.auditReport = this.repository.findAuditReportById(auditReportId);
		this.auditSections = this.repository.findAuditSectionsByAuditReportId(auditReportId);
	}

	@Override
	public void authorise() {
		boolean status;
		int auditorId;

		auditorId = super.getRequest().getPrincipal().getActiveRealm().getId();
		status = super.getRequest().getPrincipal().hasRealmOfType(Auditor.class) && this.auditReport != null && this.auditReport.getAuditor().getId() == auditorId;

		super.setAuthorised(status);
	}

	@Override
	public void unbind() {
		super.unbindObjects(this.auditSections, "name", "kind", "hours", "notes");

		super.unbindGlobal("auditReportId", this.auditReport == null ? null : this.auditReport.getId());
		super.unbindGlobal("auditReportDraftMode", this.auditReport != null && Boolean.TRUE.equals(this.auditReport.getDraftMode()));
	}

}
