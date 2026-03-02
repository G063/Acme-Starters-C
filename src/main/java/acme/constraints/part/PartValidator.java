
package acme.constraints.part;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.invention.Part;

@Validator
public class PartValidator extends AbstractValidator<ValidPart, Part> {

	@Override
	protected void initialise(final ValidPart constraintAnnotation) {
		assert constraintAnnotation != null;
	}

	@Override
	public boolean isValid(final Part entity, final ConstraintValidatorContext cvc) {
		assert cvc != null;

		if (entity == null)
			return true;

		if (entity.getCost() != null) {
			final boolean isEuro = "EUR".equals(entity.getCost().getCurrency());
			super.state(cvc, isEuro, "cost", "acme.validation.part.cost.currency");
		}

		return !super.hasErrors(cvc);
	}
}
