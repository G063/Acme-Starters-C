package acme.features.auditor.auditReport;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.helpers.MessageHelper;
import acme.client.services.AbstractService;
import acme.entities.audits.AuditReport;
import acme.realms.Auditor;

@Service
public class AuditorAuditReportListService extends AbstractService<Auditor, AuditReport> {

	@Autowired
	private AuditorAuditReportRepository repository;

	private Collection<AuditReport> auditReports;

	@Override
	public void load() {
		int auditorId;

		auditorId = super.getRequest().getPrincipal().getActiveRealm().getId();
		this.auditReports = this.repository.findAuditReportsByAuditorId(auditorId);
	}

	@Override
	public void authorise() {
		super.setAuthorised(super.getRequest().getPrincipal().hasRealmOfType(Auditor.class));
	}

	@Override
	public void unbind() {
		for (final AuditReport report : this.auditReports) {
			final Tuple tuple = super.unbindObject(report, "ticker", "name", "startMoment", "endMoment");
			final String draftLabel = Boolean.TRUE.equals(report.getDraftMode()) //
				? MessageHelper.getMessage("auditor.audit-report.form.value.draft") //
				: MessageHelper.getMessage("auditor.audit-report.form.value.published");
			tuple.put("draftMode", draftLabel);
		}
	}

}
