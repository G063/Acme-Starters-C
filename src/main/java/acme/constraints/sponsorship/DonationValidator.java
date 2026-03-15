
package acme.constraints.sponsorship;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.entities.sponsorship.Donation;

public class DonationValidator extends AbstractValidator<ValidDonation, Donation> {

	@Override
	protected void initialise(final ValidDonation constraintAnnotation) {
		assert constraintAnnotation != null;
	}

	@Override
	public boolean isValid(final Donation value, final ConstraintValidatorContext cvc) {
		assert cvc != null;

		if (value == null)
			return true;

		if (value.getMoney() != null) {
			final boolean isEuroCurrency = "EUR".equals(value.getMoney().getCurrency());
			super.state(cvc, isEuroCurrency, "cost", "acme.validation.donation.money.currency");
		}

		return !super.hasErrors(cvc);
	}
}
