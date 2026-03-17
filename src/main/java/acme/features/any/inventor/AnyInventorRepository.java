package acme.features.any.inventor;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.realms.Inventor;

@Repository
public interface AnyInventorRepository extends AbstractRepository {

	@Query("select i from Inventor i where i.id = :id")
	Inventor findInventorById(int id);

	@Query("select count(inv) from Invention inv where inv.id = :inventionId and inv.inventor.id = :inventorId and inv.draftMode = false")
	Long countPublishedInventionByInventor(int inventionId, int inventorId);
}