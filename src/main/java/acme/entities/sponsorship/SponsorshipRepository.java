
package acme.entities.sponsorship;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface SponsorshipRepository extends AbstractRepository {

	@Query("select sum(d.money.amount) from Donation d where d.sponsorship.id = :id and d.money.currency = 'EUR'")
	Double sumMoney(Integer id);

	@Query("select count(d) from Donation d where d.sponsorship.id = :id ")
	Long countSponsorshipsDonations(Integer id);

	@Query("select count(s) from Sponsorship s where s.ticker = :ticker and (:id is null or s.id <> :id)")
	Long countDuplicatedTickers(String ticker, Integer id);

	@Query("select s from Sponsorship s where s.draftMode = false order by s.startMoment desc")
	Collection<Sponsorship> findPublishedSponsorships();

	@Query("select d from Donation d where d.sponsorship.id = :sponsorshipId")
	Collection<Donation> sponsorshipDonations(int sponsorshipId);

	@Query("select s from Sponsorship s where s.id = :id and s.draftMode = false")
	Sponsorship findPublishedSponsorshipById(int id);
}
