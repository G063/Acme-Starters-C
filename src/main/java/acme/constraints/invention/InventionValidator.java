
package acme.constraints.invention;

import java.util.Collection;
import java.util.Date;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.entities.invention.Invention;
import acme.entities.invention.InventionRepository;
import acme.entities.invention.Part;

@Validator
public class InventionValidator extends AbstractValidator<ValidInvention, Invention> {

	@Autowired
	private InventionRepository inventionRepo;


	@Override
	protected void initialise(final ValidInvention constraintAnnotation) {
		assert constraintAnnotation != null;
	}

	@Override
	public boolean isValid(final Invention entity, final ConstraintValidatorContext cvc) {
		assert cvc != null;

		if (entity == null)
			return true;

		final Invention inv = this.inventionRepo.findInventionByTicker(entity.getTicker());
		final boolean isSameInvention = inv != null && inv.equals(entity);

		super.state(cvc, isSameInvention, "ticker", "acme.validation.invention.ticker.non-unique");

		if (entity.getDraftMode() != null && !entity.getDraftMode()) {
			final Collection<Part> linkedParts = this.inventionRepo.findPartsByInventionId(entity.getId());
			final boolean containsItems = linkedParts != null && !linkedParts.isEmpty();

			super.state(cvc, containsItems, "draftMode", "acme.validation.invention.parts.message");
		}

		final Date currentTime = MomentHelper.getBaseMoment();
		final Date beginDate = entity.getStartMoment();
		final Date finishDate = entity.getEndMoment();

		if (beginDate == null || finishDate == null)
			super.state(cvc, false, "startMoment", "acme.validation.invention.dates.message");
		else {
			final boolean chronologyOk = !beginDate.before(currentTime) && finishDate.after(beginDate);
			final boolean isSchemaValid = entity.getDraftMode() || chronologyOk;
			super.state(cvc, isSchemaValid, "startMoment", "acme.validation.invention.dates.message");
		}

		return !super.hasErrors(cvc);
	}
}
