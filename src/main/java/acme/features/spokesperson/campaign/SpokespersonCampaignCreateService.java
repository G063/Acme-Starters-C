package acme.features.spokesperson.campaign;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.services.AbstractService;
import acme.entities.campaign.Campaign;
import acme.realms.Spokesperson;

@Service
public class SpokespersonCampaignCreateService extends AbstractService<Spokesperson, Campaign> {

    // Atributos internos -----------------------------------------------------

    @Autowired
    private SpokespersonCampaignRepository repository;
    
    private Campaign campaign;
    

    // Interfaz AbstractService -----------------------------------------------

    @Override
    public void load() {
    	int userAccountId;
        Spokesperson spokesperson;
        
        userAccountId = super.getRequest().getPrincipal().getAccountId();
        spokesperson = this.repository.findSpokespersonByUserAccountId(userAccountId);
        
        this.campaign = super.newObject(Campaign.class);
        this.campaign.setDraftMode(true);
        this.campaign.setSpokesperson(spokesperson);
    }

    @Override
    public void authorise() {
        super.setAuthorised(true);
    }

    @Override
    public void bind() {
        super.bindObject(this.campaign, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo");
    }

    @Override
    public void validate() {
        super.validateObject(this.campaign);
    }

    @Override
    public void execute() {
    	this.campaign.setDraftMode(true);
        this.repository.save(this.campaign);
    }

    @Override
    public void unbind() {
        Tuple tuple;

        tuple = super.unbindObject(this.campaign, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo", "draftMode");
        
        tuple.put("draftMode", this.campaign.getDraftMode());
    }
}