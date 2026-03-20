package acme.features.auditor.auditReport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractService;
import acme.entities.audits.AuditReport;
import acme.realms.Auditor;

@Service
public class AuditorAuditReportShowService extends AbstractService<Auditor, AuditReport> {

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
		boolean status;
		int auditorId;

		auditorId = super.getRequest().getPrincipal().getActiveRealm().getId();
		status = super.getRequest().getPrincipal().hasRealmOfType(Auditor.class) && this.auditReport != null && this.auditReport.getAuditor().getId() == auditorId;

		super.setAuthorised(status);
	}

	@Override
	public void unbind() {
		Tuple tuple;

		tuple = super.unbindObject(this.auditReport, "id", "version", "ticker", "name", "description", "startMoment", "endMoment", "moreInfo", "draftMode");
		tuple.put("monthsActive", this.auditReport.getMonthsActive());
		tuple.put("hours", this.auditReport.getHours());
		tuple.put("draftModes", this.getDraftModeChoices(this.auditReport.getDraftMode()));
	}

	private SelectChoices getDraftModeChoices(final Boolean draftMode) {
		SelectChoices choices;

		choices = this.newObject(SelectChoices.class);
		choices.add("true", "auditor.audit-report.form.value.draft", Boolean.TRUE.equals(draftMode));
		choices.add("false", "auditor.audit-report.form.value.published", Boolean.FALSE.equals(draftMode));

		return choices;
	}

}
