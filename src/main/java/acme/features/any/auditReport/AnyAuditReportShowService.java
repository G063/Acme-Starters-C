package acme.features.any.auditReport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.principals.Any;
import acme.client.services.AbstractService;
import acme.entities.audits.AuditReport;

@Service
public class AnyAuditReportShowService extends AbstractService<Any, AuditReport> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AnyAuditReportRepository	repository;

	private AuditReport					auditReport;

	// AbstractService interface ----------------------------------------------


	@Override
	public void load() {
		int id;

		id = super.getRequest().getData("id", int.class);
		this.auditReport = this.repository.findAuditReportById(id);
	}

	@Override
	public void authorise() {
		super.setAuthorised(this.auditReport != null && !this.auditReport.getDraftMode());
	}

	@Override
	public void unbind() {

		super.unbindObject(this.auditReport, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo", //
			"auditor.userAccount.identity.name", "auditor.userAccount.identity.surname", "auditor.userAccount.identity.email", //
			"auditor.firm", "auditor.highlights", "auditor.solicitor");
		super.unbindGlobal("monthsActive", this.auditReport.getMonthsActive());
		super.unbindGlobal("hours", this.auditReport.getHours());
	}

}
