
package acme.features.any.fundraiser;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.realms.Fundraiser;

@Repository
public interface AnyFundraiserRepository extends AbstractRepository {

	@Query("select f from Fundraiser f where f.id = :id")
	Fundraiser findFundraiserById(int id);

	@Query("select count(s) from Strategy s where s.id = :strategyId and s.fundraiser.id = :fundraiserId and s.draftMode = false")
	Long countPublishedStrategyByFundraiser(int strategyId, int fundraiserId);
}
