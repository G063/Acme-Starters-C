
package acme.features.authenticated.inventor.part;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.invention.Invention;
import acme.entities.invention.Part;

@Repository
public interface InventorPartRepository extends AbstractRepository {

	@Query("select p from Part p where p.invention.id = :inventionId")
	Collection<Part> findPartsByInventionId(int inventionId);

	@Query("select i from Invention i where i.id = :id")
	Invention findOneInventionById(Integer id);

	@Query("select p from Part p where p.id = :id")
	Part findPartById(int id);
}
