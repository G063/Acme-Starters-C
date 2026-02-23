
package acme.entities;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface SponsorshipRepository extends AbstractRepository {

	@Query("select sum(d.money.amount) from Donation d where d.sponsorship.id = :id and d.money.currency = 'EUR'")
	Double sumMoney(Integer id);
}
