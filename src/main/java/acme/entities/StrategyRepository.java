
package acme.entities;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface StrategyRepository extends AbstractRepository {

	@Query("select sum(t.expectedPercentage) from Tactic t where t.strategy.id = :strategyId")
	Double computeExpectedPercentage(int strategyId);

	@Query("select count(t) from Tactic t where t.strategy.id = :strategyId")
	Integer countTacticsByStrategyId(int strategyId);

}
