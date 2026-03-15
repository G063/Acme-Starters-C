
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

	@Query("select sp from Sponsor sp where sp.id = :sponsorId")
	Sponsor findSponsorBySponsorId(int sponsorId);

	@Query("select s from Sponsorship s where s.sponsor.id = :sponsorId order by s.startMoment desc")
	Collection<Sponsorship> findSponsorshipsBySponsorId(int sponsorId);

	@Query("select s from Sponsorship s where s.id = :id")
	Sponsorship findSponsorshipById(int id);

	@Query("select d from Donation d where d.sponsorship.id = :sponsorshipId")
	Collection<Donation> findDonationsBySponsorshipId(int sponsorshipId);
}
