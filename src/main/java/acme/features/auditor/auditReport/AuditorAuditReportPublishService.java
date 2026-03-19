package acme.features.auditor.auditReport;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.entities.audits.AuditReport;
import acme.entities.audits.AuditSection;
import acme.realms.Auditor;

@Service
public class AuditorAuditReportPublishService extends AbstractService<Auditor, AuditReport> {

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
		super.bindObject(this.auditReport, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo");
	}

	@Override
	public void validate() {
		Date baseMoment = MomentHelper.getBaseMoment();
		Date start = this.auditReport.getStartMoment();

		if (start != null)
			super.state(MomentHelper.isAfter(start, baseMoment), "startMoment", "acme.validation.auditReport.minimum-start.error");

		this.auditReport.setDraftMode(false);
		super.validateObject(this.auditReport);
		this.auditReport.setDraftMode(true);

		Collection<AuditSection> sections = this.repository.findAuditSectionsByAuditReportId(this.auditReport.getId());
		super.state(sections != null && !sections.isEmpty(), "*", "acme.validation.auditReport.auditSection.error");
	}

	@Override
	public void execute() {
		this.auditReport.setDraftMode(false);
		this.repository.save(this.auditReport);

		super.getResponse().setView("redirect:/auditor/audit-report/list");
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
