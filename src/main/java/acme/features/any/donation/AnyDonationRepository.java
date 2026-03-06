package acme.features.any.donation;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.sponsorship.Donation;

@Repository
public interface AnyDonationRepository extends AbstractRepository {

	@Query("select d from Donation d where d.sponsorship.id = :sponsorshipId and d.sponsorship.draftMode = false order by d.id asc")
	Collection<Donation> findPublishedDonationsBySponsorshipId(int sponsorshipId);

	@Query("select count(s) from Sponsorship s where s.id = :sponsorshipId and s.draftMode = false")
	Long countPublishedSponsorshipById(int sponsorshipId);
}
