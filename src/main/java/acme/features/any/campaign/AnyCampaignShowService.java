package acme.features.any.campaign;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.principals.Any;
import acme.client.services.AbstractService;
import acme.entities.campaign.Campaign;

@Service
public class AnyCampaignShowService extends AbstractService<Any, Campaign>{

    @Autowired
    protected AnyCampaignRepository repository;
    protected Campaign campaign;

    @Override
    public void authorise() {
        super.getResponse().setAuthorised(true);
    }

    @Override
    public void load() {

        int id;

        id = super.getRequest().getData("id", int.class);
        campaign = this.repository.findCampaignById(id);

    }

    @Override
    public void unbind() {


        super.unbindObject(campaign,
            "ticker",
            "name",
            "description",
            "startMoment",
            "endMoment",
            "moreInfo");

    }
}