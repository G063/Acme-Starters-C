package acme.features.any.spokesperson;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.realms.Spokesperson;

@Repository
public interface AnySpokespersonRepository extends AbstractRepository {
	
	@Query("select s from Spokesperson s where s.id = :id")
	Spokesperson findSpokespersonById(int id);

	@Query("select count(c) from Campaign c where c.id = :campaignId and c.spokesperson.id = :spokespersonId and c.draftMode = false")
	Long countPublishedCampaignBySpokesperson(int campaignId, int spokespersonId);
}
