
package acme.constraints.audit;

import java.util.Date;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.entities.audits.AuditReport;
import acme.entities.audits.AuditReportRepository;

@Validator
public class AuditReportValidator extends AbstractValidator<ValidAuditReport, AuditReport> {

	@Autowired
	private AuditReportRepository repository;


	@Override
	public void initialize(final ValidAuditReport constraintAnnotation) {
		assert constraintAnnotation != null;
	}

	@Override
	public boolean isValid(final AuditReport auditReport, final ConstraintValidatorContext context) {

		assert context != null;
		if (auditReport == null)
			return true;

		boolean isPublished = auditReport.getDraftMode() != null && !auditReport.getDraftMode();

		if (auditReport.getTicker() != null) {
			AuditReport existing = this.repository.findByTicker(auditReport.getTicker());
			boolean uniqueTicker = existing == null || existing.getId() == auditReport.getId();
			super.state(context, uniqueTicker, "ticker", "acme.validation.auditReport.ticker.non-unique");
		}

		Date start = auditReport.getStartMoment();
		Date end = auditReport.getEndMoment();
		Date minimumDraftStart = MomentHelper.parse("2000/01/01 00:00", "yyyy/MM/dd HH:mm");

		if (start != null && end != null) {
			boolean hasValidInterval = end.getTime() - start.getTime() >= 60L * 1000L;
			super.state(context, hasValidInterval, "startMoment", "acme.validation.auditReport.dates.error");
		}

		if (start != null && auditReport.getDraftMode() != null) {
			boolean hasValidMinimumStart;

			if (auditReport.getDraftMode())
				hasValidMinimumStart = !start.before(minimumDraftStart);
			else {
				Date baseMoment = MomentHelper.getBaseMoment();
				hasValidMinimumStart = MomentHelper.isAfter(start, baseMoment);
			}

			super.state(context, hasValidMinimumStart, "startMoment", "acme.validation.auditReport.minimum-start.error");
		}

		if (isPublished && auditReport.getId() != 0) {
			Integer sectionCount = this.repository.countAuditSections(auditReport.getId());
			boolean hasSections = sectionCount != null && sectionCount > 0;
			super.state(context, hasSections, "*", "acme.validation.auditReport.auditSection.error");
		}

		return !super.hasErrors(context);
	}
}
