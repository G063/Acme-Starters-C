package acme.features.sponsor.sponsorship;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.sponsorship.Donation;
import acme.entities.sponsorship.Sponsorship;
import acme.realms.Sponsor;

@Repository
public interface SponsorSponsorshipRepository extends AbstractRepository {

	@Query("select sp from Sponsor sp where sp.userAccount.id = :userAccountId")
	Sponsor findSponsorByUserAccountId(int userAccountId);

	@Query("select s from Sponsorship s where s.sponsor.userAccount.id = :userAccountId order by s.startMoment desc")
	Collection<Sponsorship> findSponsorshipsByUserAccountId(int userAccountId);

	@Query("select s from Sponsorship s where s.id = :id")
	Sponsorship findSponsorshipById(int id);

	@Query("select count(s) from Sponsorship s where s.id = :id and s.sponsor.userAccount.id = :userAccountId")
	Long countOwnedSponsorshipById(int id, int userAccountId);

	@Query("select count(s) from Sponsorship s where s.id = :id and s.sponsor.userAccount.id = :userAccountId and s.draftMode = true")
	Long countOwnedDraftSponsorshipById(int id, int userAccountId);

	@Query("select d from Donation d where d.sponsorship.id = :sponsorshipId")
	Collection<Donation> findDonationsBySponsorshipId(int sponsorshipId);
}

