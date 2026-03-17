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
    	int spokespersonId;
        Spokesperson spokesperson;
        
        spokespersonId = super.getRequest().getPrincipal().getActiveRealm().getId();
        spokesperson = this.repository.findSpokespersonByCampaignId(spokespersonId);
        
        this.campaign = super.newObject(Campaign.class);
        this.campaign.setDraftMode(true);
        this.campaign.setSpokesperson(spokesperson);
    }

    @Override
    public void authorise() {
    	boolean status;

		status = super.getRequest().getPrincipal().hasRealmOfType(Spokesperson.class);
		super.setAuthorised(status);
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