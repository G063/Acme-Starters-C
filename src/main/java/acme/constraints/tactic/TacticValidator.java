
package acme.constraints.tactic;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.strategy.Tactic;

@Validator
public class TacticValidator extends AbstractValidator<ValidTactic, Tactic> {

	@Override
	protected void initialise(final ValidTactic constraintAnnotation) {
		assert constraintAnnotation != null;
	}

	@Override
	public boolean isValid(final Tactic entity, final ConstraintValidatorContext cvc) {
		assert cvc != null;

		if (entity == null)
			return true;

		return !super.hasErrors(cvc);
	}
}
