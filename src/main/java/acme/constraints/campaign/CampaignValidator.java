package acme.constraints.campaign;

import java.util.Date;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.entities.campaign.Campaign;
import acme.entities.campaign.CampaignRepository;

@Validator
public class CampaignValidator extends AbstractValidator<ValidCampaign, Campaign>{

	@Autowired
	private CampaignRepository repository;
	
	@Override
	protected void initialise(final ValidCampaign annotation) {
		assert annotation != null;
	}
	
	@Override
	public boolean isValid(final Campaign campaign, final ConstraintValidatorContext context) {
	    assert context != null;

	    if (campaign == null)
	        return true;

	    boolean result = true;
	    final boolean published = campaign.getDraftMode() != null && !campaign.getDraftMode();
	    final Date now = MomentHelper.getBaseMoment();

	    // startMoment/endMoment must be a valid interval.
		boolean validInterval;
		if (campaign.getStartMoment() != null && campaign.getEndMoment() != null) {
			validInterval = MomentHelper.isBefore(campaign.getStartMoment(), campaign.getEndMoment());

			if (!validInterval) {
				super.state(context, false, "*", "acme.validation.campaign.invalid-interval.message");
				result = false;
			}
		}

		if (published && campaign.getStartMoment() != null) {
			final boolean startInFuture = !campaign.getStartMoment().before(now);
			if (!startInFuture) {
				super.state(context, false, "startMoment", "acme.validation.campaign.future-dates.message");
				result = false;
			}
		}
	    
		
		// Ticker unico
	    final int id = campaign.getId();
	    
	    if (campaign.getTicker() != null) {
	    	final long duplicates = this.repository.countOtherCampaignsWithTicker(campaign.getTicker(), id);
	    	final boolean isUnique = duplicates == 0;
	    	if (!isUnique) {
	    		super.state(context, false, "ticker", "acme.validation.campaign.ticker.non-unique");
	    		result = false;
			}
		}


	    // Campaigns cannot be published unless they have at least one milestone.
	    if (campaign.getDraftMode() != null && !campaign.getDraftMode() && campaign.getId() != 0) {

	        Double sum = this.repository.sumEffortByCampaignId(campaign.getId());
	        boolean hasMilestones = sum != null;

	        super.state(context, hasMilestones, "*",
	            "acme.validation.campaign.no-milestones");

	        if (!hasMilestones)
	            result = false;
	    }

	    return result;
	}
	
}
