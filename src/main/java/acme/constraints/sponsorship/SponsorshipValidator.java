
package acme.constraints.sponsorship;

import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.helpers.MomentHelper;
import acme.entities.sponsorship.Sponsorship;
import acme.entities.sponsorship.SponsorshipRepository;

public class SponsorshipValidator extends AbstractValidator<ValidSponsorship, Sponsorship> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private SponsorshipRepository repository;

	// ConstraintValidator interface ------------------------------------------


	@Override
	protected void initialise(final ValidSponsorship annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Sponsorship value, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (value == null)
			result = true;

		else {
			{
				boolean uniqueSponsorship;
				Sponsorship existingSponsorship;

				existingSponsorship = this.repository.findSponsorshipByTicker(value.getTicker());
				uniqueSponsorship = existingSponsorship == null || existingSponsorship.equals(value);

				super.state(context, uniqueSponsorship, "ticker", "acme.validation.duplicated-ticker.message");

			}
			{
				// 1. Sponsorships must have at least one donation to be published
				boolean notPublished;
				Integer id;
				long donations;
				boolean validDonations;

				notPublished = value.getDraftMode() != null && value.getDraftMode().equals(false);

				if (notPublished) {
					id = value.getId();
					donations = id == null ? 0L : this.repository.countSponsorshipsDonations(id);
					validDonations = donations >= 1L;
					super.state(context, validDonations, "draftMode", "acme.validation.sponshorship.no-donations.message");

				}
			}
			{
				// 2. startMoment/endMoment must be a valid time interval in future wrt. the moment when a sponsorship is published
				// end > start
				boolean validStartMoment;
				boolean validEndMoment;
				boolean published;
				Date minimumInterval;

				published = value.getDraftMode() != null && value.getDraftMode().equals(true);

				if (published && value.getStartMoment() != null && value.getEndMoment() != null) {
					minimumInterval = MomentHelper.deltaFromBaseMoment(1, ChronoUnit.MINUTES);
					validStartMoment = MomentHelper.isAfter(value.getStartMoment(), minimumInterval);
					validEndMoment = MomentHelper.isAfter(value.getEndMoment(), value.getStartMoment());
					super.state(context, validStartMoment, "startMoment", "acme.validation.sponsorship.invalid-interval.message");
					super.state(context, validEndMoment, "endMoment", "acme.validation.sponsorship.invalid-interval.message");
				}
			}
			result = !super.hasErrors(context);
		}
		return result;

	}

}
