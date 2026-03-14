package acme.features.any.spokesperson;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;
import acme.realms.Spokesperson;

public interface AnySpokespersonRepository extends AbstractRepository {
	
	@Query("select s from Spokesperson s where s.id = :id")
	Spokesperson findSpokespersonById(int id);
}
