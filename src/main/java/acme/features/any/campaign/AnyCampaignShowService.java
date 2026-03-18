package acme.features.any.campaign;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
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
        super.setAuthorised(this.campaign != null && !this.campaign.getDraftMode());;
    }

    @Override
    public void load() {

        int id;

        id = super.getRequest().getData("id", int.class);
        this.campaign = this.repository.findCampaignById(id);

    }

    @Override
    public void unbind() {
        Tuple tuple;
        Double effort;

        tuple = super.unbindObject(this.campaign,
            "ticker",
            "name",
            "description",
            "startMoment",
            "endMoment",
            "moreInfo");

        effort = this.repository.sumEffortByCampaignId(this.campaign.getId());
        tuple.put("monthsActive", this.campaign.getMonthsActive());
        tuple.put("effort", effort == null ? 0.0 : effort);

    }
}