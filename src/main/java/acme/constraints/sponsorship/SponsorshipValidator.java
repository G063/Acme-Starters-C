
package acme.constraints.sponsorship;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
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
			result = !super.hasErrors(context);
		}
		return result;

	}

}
