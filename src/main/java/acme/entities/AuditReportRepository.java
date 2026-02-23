package acme.entities;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface AuditReportRepository extends AbstractRepository {

    @Query("select sum(s.hours) from AuditSection s where s.auditReport.id = :id")
    Integer computeAuditReportHours(Integer id);

    @Query("select count(s) from AuditSection s where s.auditReport.id = :id")
    Integer countAuditSections(Integer id);

}
