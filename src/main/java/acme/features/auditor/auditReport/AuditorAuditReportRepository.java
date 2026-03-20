package acme.features.auditor.auditReport;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.audits.AuditReport;
import acme.entities.audits.AuditSection;
import acme.realms.Auditor;

@Repository
public interface AuditorAuditReportRepository extends AbstractRepository {

	@Query("select a from Auditor a where a.id = :auditorId")
	Auditor findAuditorById(int auditorId);

	@Query("select a from Auditor a where a.userAccount.id = :userAccountId")
	Auditor findAuditorByUserAccountId(int userAccountId);

	@Query("select ar from AuditReport ar where ar.auditor.id = :auditorId order by ar.startMoment desc")
	Collection<AuditReport> findAuditReportsByAuditorId(int auditorId);

	@Query("select ar from AuditReport ar where ar.id = :id")
	AuditReport findAuditReportById(int id);

	@Query("select s from AuditSection s where s.auditReport.id = :auditReportId")
	Collection<AuditSection> findAuditSectionsByAuditReportId(int auditReportId);

}
