package acme.features.any.sponsor;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.realms.Sponsor;

@Repository
public interface AnySponsorRepository extends AbstractRepository {

	@Query("select sp from Sponsor sp where sp.id = :id")
	Sponsor findSponsorById(int id);

	@Query("select count(s) from Sponsorship s where s.id = :sponsorshipId and s.sponsor.id = :sponsorId and s.draftMode = false")
	Long countPublishedSponsorshipBySponsor(int sponsorshipId, int sponsorId);
}
