package acme.features.auditor.auditSection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.services.AbstractService;
import acme.entities.audits.AuditSection;
import acme.realms.Auditor;

@Service
public class AuditorAuditSectionDeleteService extends AbstractService<Auditor, AuditSection> {

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
		int auditorId;
		boolean isOwner;
		boolean isDraft;

		auditorId = super.getRequest().getPrincipal().getActiveRealm().getId();
		isOwner = this.auditSection != null && this.auditSection.getAuditReport().getAuditor().getId() == auditorId;
		isDraft = this.auditSection != null && Boolean.TRUE.equals(this.auditSection.getAuditReport().getDraftMode());

		super.setAuthorised(super.getRequest().getPrincipal().hasRealmOfType(Auditor.class) && isOwner && isDraft);
	}

	@Override
	public void bind() {
	}

	@Override
	public void validate() {
	}

	@Override
	public void execute() {
		this.repository.delete(this.auditSection);

		super.getResponse().setView("redirect:/auditor/audit-section/list?auditReportId=" + this.auditSection.getAuditReport().getId());
	}

	@Override
	public void unbind() {
		Tuple tuple;

		tuple = super.unbindObject(this.auditSection, "name", "notes", "hours", "kind");
		tuple.put("auditReportId", this.auditSection.getAuditReport().getId());
		tuple.put("auditReportDraftMode", Boolean.TRUE.equals(this.auditSection.getAuditReport().getDraftMode()));
	}

}
