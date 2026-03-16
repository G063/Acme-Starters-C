package acme.features.any.campaign;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.principals.Any;
import acme.client.services.AbstractService;
import acme.entities.campaign.Campaign;

@Service
public class AnyCampaignListService extends AbstractService<Any, Campaign>{

    @Autowired
    protected AnyCampaignRepository repository;
    
    protected Collection<Campaign> campaign;

    @Override
    public void authorise() {
        super.setAuthorised(true);
    }

    @Override
    public void load() {

        this.campaign = this.repository.findPublishedCampaigns();
    }

    @Override
    public void unbind() {

    	super.unbindObjects(this.campaign,
            "ticker",
            "name",
            "startMoment",
            "endMoment");

    }
}
