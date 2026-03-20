package acme.features.auditor.auditReport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractService;
import acme.entities.audits.AuditReport;
import acme.realms.Auditor;

@Service
public class AuditorAuditReportCreateService extends AbstractService<Auditor, AuditReport> {

	@Autowired
	private AuditorAuditReportRepository repository;

	private AuditReport auditReport;

	@Override
	public void load() {
		int auditorId;
		Auditor auditor;

		auditorId = super.getRequest().getPrincipal().getActiveRealm().getId();
		auditor = this.repository.findAuditorById(auditorId);

		this.auditReport = this.newObject(AuditReport.class);
		this.auditReport.setDraftMode(true);
		this.auditReport.setAuditor(auditor);
	}

	@Override
	public void authorise() {
		super.setAuthorised(super.getRequest().getPrincipal().hasRealmOfType(Auditor.class));
	}

	@Override
	public void bind() {
		super.bindObject(this.auditReport, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo");
	}

	@Override
	public void validate() {
		super.validateObject(this.auditReport);
	}

	@Override
	public void execute() {
		this.auditReport.setDraftMode(true);
		this.repository.save(this.auditReport);
	}

	@Override
	public void unbind() {
		Tuple tuple;

		tuple = super.unbindObject(this.auditReport, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo", "draftMode");
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
