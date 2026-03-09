
package acme.entities.invention;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface InventionRepository extends AbstractRepository {

	@Query("select sum(p.cost.amount) from Part p where p.invention.id = :id")
	Double computeInventionAmount(Integer id);

	@Query("select i from Invention i where i.ticker = :ticker")
	Invention findInventionByTicker(String ticker);

	@Query("select p from Part p where p.invention.id = :id")
	Collection<Part> findPartsByInventionId(Integer id);

}
