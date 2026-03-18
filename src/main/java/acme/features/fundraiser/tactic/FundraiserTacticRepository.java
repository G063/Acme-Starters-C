
package acme.features.fundraiser.tactic;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.strategy.Strategy;
import acme.entities.strategy.Tactic;

@Repository
public interface FundraiserTacticRepository extends AbstractRepository {

	@Query("select t from Tactic t where t.id = :id")
	Tactic findOneTacticById(int id);

	@Query("select t from Tactic t where t.strategy.id = :strategyId")
	Collection<Tactic> findManyTacticsByStrategyId(int strategyId);

	@Query("select s from Strategy s where s.id = :id")
	Strategy findOneStrategyById(int id);

}
