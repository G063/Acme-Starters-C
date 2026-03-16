
package acme.features.fundraiser.strategy;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.strategy.Strategy;
import acme.entities.strategy.Tactic;
import acme.realms.Fundraiser;

@Repository
public interface FundraiserStrategyRepository extends AbstractRepository {

	@Query("select s from Strategy s where s.fundraiser.id = :fundraiserId")
	Collection<Strategy> findStrategiesByFundraiserId(int fundraiserId);

	@Query("select s from Strategy s where s.id = :id")
	Strategy findOneStrategyById(int id);

	@Query("select f from Fundraiser f where f.id = :id")
	Fundraiser findFundraiserById(int id);

	@Query("select count(t) from Tactic t where t.strategy.id = :id")
	int countTacticsByStrategyId(int id);

	@Query("select t from Tactic t where t.strategy.id = :id")
	Collection<Tactic> findTacticsByStrategyId(int id);

}
