
package acme.constraints.sponsorship;

import java.util.Collection;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.helpers.MomentHelper;
import acme.entities.sponsorship.Donation;
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
			boolean published;

			published = value.getDraftMode() != null && value.getDraftMode().equals(false);
			{
				// 1. Sponsorship's tickers are unique  
				boolean uniqueSponsorship;
				Long duplicatedTickers;

				if (value.getTicker() == null || value.getTicker().isBlank())
					uniqueSponsorship = true;
				else {
					duplicatedTickers = this.repository.countDuplicatedTickers(value.getTicker(), value.getId());
					uniqueSponsorship = duplicatedTickers == null || duplicatedTickers == 0L;
				}

				super.state(context, uniqueSponsorship, "ticker", "acme.validation.duplicated-ticker.message");

			}
			{
				// 2. Sponsorships must have at least one donation to be published
				Integer id;
				long donations;
				boolean validDonations;

				if (published) {
					id = value.getId();
					donations = id == null ? 0L : this.repository.countSponsorshipsDonations(id);
					validDonations = donations >= 1L;
					super.state(context, validDonations, "draftMode", "acme.validation.sponsorship.no-donations.message");

				}
			}
			{
				// 3. startMoment/endMoment must be a valid time interval in future wrt. the moment when a sponsorship is published
				// end > start
				boolean validInterval;

				published = value.getDraftMode() != null && value.getDraftMode().equals(false);
				if (published && value.getStartMoment() != null && value.getEndMoment() != null) {
					validInterval = MomentHelper.isBefore(value.getStartMoment(), value.getEndMoment());
					super.state(context, validInterval, "startMoment", "acme.validation.sponsorship.invalid-interval.message");
					super.state(context, validInterval, "endMoment", "acme.validation.sponsorship.invalid-interval.message");
				}
			}
			{
				// 4. All donations of a sponsorship must be in EUR
				boolean allDonationsEUR;
				Collection<Donation> donations;

				if (published && (Integer) value.getId() != null) {

					donations = this.repository.sponsorshipDonations(value.getId());

					allDonationsEUR = donations.stream().allMatch(d -> "EUR".equals(d.getMoney().getCurrency()));

					super.state(context, allDonationsEUR, "draftMode", "acme.validation.donation.money.currency.message");
				}
			}
			result = !super.hasErrors(context);
		}
		return result;

	}

}
