package acme.features.spokesperson.campaign;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.campaign.Campaign;
import acme.entities.campaign.Milestone;
import acme.realms.Spokesperson;

@Repository
public interface SpokespersonCampaignRepository extends AbstractRepository {

    @Query("select s from Spokesperson s where s.userAccount.id = :userAccountId")
    Spokesperson findSpokespersonByUserAccountId(int userAccountId);

    @Query("select c from Campaign c where c.id = :id")
    Campaign findCampaignById(int id);

    @Query("select c from Campaign c where c.spokesperson.id = :spokespersonId")
    Collection<Campaign> findCampaignsBySpokespersonId(int spokespersonId);

    @Query("select m from Milestone m where m.campaign.id = :campaignId")
    Collection<Milestone> findMilestonesByCampaignId(int campaignId);

    @Query("select sum(m.effort) from Milestone m where m.campaign.id = :campaignId")
    Double computeEffortByCampaignId(int campaignId);
    
    @Query("select count(m) from Milestone m where m.campaign.id = :campaignId")
    Integer countMilestonesByCampaignId(int campaignId);

    @Query("select c from Campaign c where c.ticker = :ticker")
    Campaign findCampaignByTicker(String ticker);

    @Query("select c from Campaign c where c.id = :spokespersonId")
	Spokesperson findSpokespersonByCampaignId(int spokespersonId);
}
