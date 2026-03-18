package acme.features.any.auditReport;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.audits.AuditReport;
import acme.realms.Auditor;

@Repository
public interface AnyAuditReportRepository extends AbstractRepository {

	@Query("select ar from AuditReport ar where ar.draftMode = false")
	Collection<AuditReport> findPublishedAuditReports();

	@Query("select ar from AuditReport ar where ar.id = :id")
	AuditReport findAuditReportById(int id);

	@Query("select a from Auditor a where a.id = :id")
	Auditor findAuditorById(int id);

}
