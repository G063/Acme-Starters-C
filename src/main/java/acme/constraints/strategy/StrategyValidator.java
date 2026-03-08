
package acme.constraints.strategy;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.entities.strategy.Strategy;
import acme.entities.strategy.StrategyRepository;

@Validator
public class StrategyValidator extends AbstractValidator<ValidStrategy, Strategy> {

	@Autowired
	private StrategyRepository repository;


	@Override
	protected void initialise(final ValidStrategy annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Strategy strategy, final ConstraintValidatorContext context) {
		assert strategy != null;

		boolean result = true;

		if (strategy.getStartMoment() != null && strategy.getEndMoment() != null) {
			boolean intervalValid = MomentHelper.isBefore(strategy.getStartMoment(), strategy.getEndMoment());

			super.state(context, intervalValid, "endMoment", "acme.validation.strategy.invalid-interval");

			if (!intervalValid)
				result = false;
		}

		if (strategy.getDraftMode() != null && !strategy.getDraftMode()) {

			boolean hasTactics = false;

			if (strategy.getId() != 0) {
				Integer tacticsCount = this.repository.countTacticsByStrategyId(strategy.getId());
				hasTactics = tacticsCount != null && tacticsCount > 0L;
			}

			super.state(context, hasTactics, "draftMode", "acme.validation.strategy.no-tactics");

			if (!hasTactics)
				result = false;
		}

		return result;
	}
}
