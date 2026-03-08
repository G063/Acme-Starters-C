
package acme.constraints.sponsorship;

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

		boolean result = true;

		if (value == null)
			return result;

		final boolean published = value.getDraftMode() != null && !value.getDraftMode();
		final Integer id = value.getId();

		// 1. Sponsorships must have at least one donation to be published
		if (published) {
			final long donations = id == null ? 0L : this.repository.countSponsorshipsDonations(id);
			if (donations < 1L) {
				super.state(context, false, "draftMode", "acme.validation.sponshorship.no-donations.message");
				result = false;
			}
		}

		// 2. startMoment/endMoment must be a valid time interval in future wrt. the moment when a sponsorship is notPublished
		// end > start

		if (published && value.getStartMoment() != null && value.getEndMoment() != null) {
			final Date now = MomentHelper.getCurrentMoment();
			if (!(value.getStartMoment().after(now) && value.getEndMoment().after(value.getStartMoment()))) {
				super.state(context, false, "startMoment", "acme.validation.sponsorship.invalid-interval.message");
				super.state(context, false, "endMoment", "acme.validation.sponsorship.invalid-interval.message");
				result = false;
			}
		}

		// 3. The total money of a sponsorship is the sum of money in the corresponding donations. Only Euros are accepted

		if (published && id != null && this.repository.countNonEurDonations(id) > 0L) {
			super.state(context, false, "draftMode", "acme.validation.sponsorship.only-eur.message");
			result = false;
		}

		return result;

	}

}
