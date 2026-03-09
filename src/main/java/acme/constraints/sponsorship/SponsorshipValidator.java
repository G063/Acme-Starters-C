
package acme.constraints.sponsorship;

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
				boolean published;
				Integer id;
				long donations;
				boolean validDonations;

				published = value.getDraftMode() != null && value.getDraftMode().equals(false);

				if (published) {
					id = value.getId();
					donations = id == null ? 0L : this.repository.countSponsorshipsDonations(id);
					validDonations = donations >= 1L;
					super.state(context, validDonations, "draftMode", "acme.validation.sponshorship.no-donations.message");

				}
			}
			{
				// 2. startMoment/endMoment must be a valid time interval in future wrt. the moment when a sponsorship is published
				// end > start
				boolean validInterval;
				boolean published;

				published = value.getDraftMode() != null && value.getDraftMode().equals(false);
				if (published && value.getStartMoment() != null && value.getEndMoment() != null) {
					validInterval = MomentHelper.isBefore(value.getStartMoment(), value.getEndMoment());
					super.state(context, validInterval, "startMoment", "acme.validation.sponsorship.invalid-interval.message");
				}
			}
			result = !super.hasErrors(context);
		}
		return result;

	}

}
