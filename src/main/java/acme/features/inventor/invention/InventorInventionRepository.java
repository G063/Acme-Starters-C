
package acme.features.inventor.invention;

import java.util.Collection;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.invention.Invention;
import acme.realms.Inventor;

@Repository
public interface InventorInventionRepository extends AbstractRepository {

	@Query("select i from Inventor i where i.id = :id")
	Inventor findInventorById(Integer id);

	@Query("select i from Inventor i where i.userAccount.id = :id")
	Inventor findInventorByUserAccountId(Integer id);

	@Query("select i from Invention i where i.ticker = :ticker")
	Invention findInventionByTicker(String ticker);

	@Query("select i from Invention i where i.id = :id")
	Invention findOneInventionById(Integer id);

	@Modifying
	@Query("delete from Part p where p.invention.id = :id")
	void deletePartsByInventionId(Integer id);

	@Query("select i from Invention i where i.inventor.id = :inventorId")
	Collection<Invention> findInventionsByInventorId(Integer inventorId);

	@Query("select count(p) from Part p where p.invention.id = :id")
	int countPartsByInventionId(int id);

}
