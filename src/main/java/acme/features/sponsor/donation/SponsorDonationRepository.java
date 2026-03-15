package acme.features.sponsor.donation;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.sponsorship.Donation;
import acme.entities.sponsorship.Sponsorship;

@Repository
public interface SponsorDonationRepository extends AbstractRepository {

	@Query("select s from Sponsorship s where s.id = :id")
	Sponsorship findSponsorshipById(int id);

	@Query("select d from Donation d where d.sponsorship.id = :sponsorshipId order by d.id asc")
	Collection<Donation> findDonationsBySponsorshipId(int sponsorshipId);

	@Query("select d from Donation d where d.id = :id")
	Donation findDonationById(int id);

	@Query("select count(s) from Sponsorship s where s.id = :id and s.sponsor.userAccount.id = :userAccountId")
	Long countOwnedSponsorshipById(int id, int userAccountId);

	@Query("select count(s) from Sponsorship s where s.id = :id and s.sponsor.userAccount.id = :userAccountId and s.draftMode = true")
	Long countOwnedDraftSponsorshipById(int id, int userAccountId);

	@Query("select count(d) from Donation d where d.id = :id and d.sponsorship.sponsor.userAccount.id = :userAccountId")
	Long countOwnedDonationById(int id, int userAccountId);

	@Query("select count(d) from Donation d where d.id = :id and d.sponsorship.sponsor.userAccount.id = :userAccountId and d.sponsorship.draftMode = true")
	Long countOwnedDraftDonationById(int id, int userAccountId);
}

