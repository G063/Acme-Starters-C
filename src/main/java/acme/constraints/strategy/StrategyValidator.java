
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

		if (strategy.getTicker() != null) {
			final Strategy existing = this.repository.findStrategyByTicker(strategy.getTicker());

			final boolean isUnique = existing == null || existing.getId() == strategy.getId();

			super.state(context, isUnique, "ticker", "acme.validation.strategy.ticker.non-unique");

			if (!isUnique)
				result = false;
		}

		if (strategy.getStartMoment() != null && strategy.getEndMoment() != null) {

			final boolean intervalValid = MomentHelper.isBefore(strategy.getStartMoment(), strategy.getEndMoment());

			super.state(context, intervalValid, "endMoment", "acme.validation.strategy.invalid-interval");

			if (!intervalValid)
				result = false;
		}

		if (strategy.getDraftMode() != null && !strategy.getDraftMode()) {

			boolean hasTactics = false;

			if (strategy.getId() != 0) {
				final Integer tacticsCount = this.repository.countTacticsByStrategyId(strategy.getId());
				hasTactics = tacticsCount != null && tacticsCount > 0;
			}

			super.state(context, hasTactics, "draftMode", "acme.validation.strategy.no-tactics");

			if (!hasTactics)
				result = false;
		}

		return result;
	}
}
