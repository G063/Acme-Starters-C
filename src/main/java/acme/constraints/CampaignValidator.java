package acme.constraints;

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

	    // 1. start < end
	    if (campaign.getStartMoment() != null && campaign.getEndMoment() != null) {

	        boolean intervalValid =
	            MomentHelper.isBefore(campaign.getStartMoment(), campaign.getEndMoment());

	        super.state(context, intervalValid, "endMoment",
	            "acme.validation.campaign.invalid-interval");

	        if (!intervalValid)
	            result = false;
	    }

	    // 2. moments in the future
	    if (campaign.getStartMoment() != null) {
	        boolean startFuture = MomentHelper.isFuture(campaign.getStartMoment());

	        super.state(context, startFuture, "startMoment",
	            "acme.validation.campaign.not-future");

	        if (!startFuture)
	            result = false;
	    }

	    if (campaign.getEndMoment() != null) {
	        boolean endFuture = MomentHelper.isFuture(campaign.getEndMoment());

	        super.state(context, endFuture, "endMoment",
	            "acme.validation.campaign.not-future");

	        if (!endFuture)
	            result = false;
	    }

	    // 3. published campaign must have milestones
	    if (Boolean.FALSE.equals(campaign.getDraftMode()) && campaign.getId() != 0) {

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
