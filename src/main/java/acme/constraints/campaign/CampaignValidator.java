package acme.constraints.campaign;

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
	    assert campaign != null;

	    boolean result = true;

	    // startMoment < endMoment
	    if (campaign.getStartMoment() != null && campaign.getEndMoment() != null) {

	        boolean intervalValid =
	            MomentHelper.isBefore(campaign.getStartMoment(), campaign.getEndMoment());

	        super.state(context, intervalValid, "endMoment",
	            "acme.validation.campaign.invalid-interval");

	        if (!intervalValid)
	            result = false;
	    }

	    // startMoment/endMoment must be a valid time interval in future wrt. the moment when a campaign is published.
	    if (campaign.getStartMoment() != null) {
	        boolean startFuture = MomentHelper.isFuture(campaign.getStartMoment());

	        super.state(context, startFuture, "startMoment",
	            "acme.validation.campaign.not-future");

	        if (!startFuture)
	            result = false;
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
