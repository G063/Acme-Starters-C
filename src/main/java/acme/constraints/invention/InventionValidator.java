
package acme.constraints.invention;

import java.util.Collection;
import java.util.Date;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.helpers.MomentHelper;
import acme.entities.invention.Invention;
import acme.entities.invention.InventionRepository;
import acme.entities.invention.Part;

public class InventionValidator extends AbstractValidator<ValidInvention, Invention> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private InventionRepository inventionRepo;

	// ConstraintValidator interface ------------------------------------------


	@Override
	protected void initialise(final ValidInvention annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Invention value, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result = true;

		if (value == null)
			return result;

		final boolean published = value.getDraftMode() != null && !value.getDraftMode();
		final int id = value.getId();

		if (value.getTicker() != null) {
			final Invention inv = this.inventionRepo.findInventionByTicker(value.getTicker());
			final boolean isUnique = inv == null || inv.getId() == id;
			if (!isUnique) {
				super.state(context, false, "ticker", "acme.validation.invention.ticker.non-unique");
				result = false;
			}
		}

		if (published)
			if (id != 0) {
				final Collection<Part> linkedParts = this.inventionRepo.findPartsByInventionId(id);
				final boolean containsItems = linkedParts != null && !linkedParts.isEmpty();
				if (!containsItems) {
					super.state(context, false, "draftMode", "acme.validation.invention.parts.message");
					result = false;
				}
			} else {
				super.state(context, false, "draftMode", "acme.validation.invention.parts.message");
				result = false;
			}

		if (value.getStartMoment() != null && value.getEndMoment() != null) {

			final Date reference = MomentHelper.getBaseMoment();

			final boolean futureOk = !value.getStartMoment().before(reference);

			final boolean chronologyOk = value.getEndMoment().after(value.getStartMoment());

			if (!futureOk) {
				super.state(context, false, "startMoment", "acme.validation.invention.dates.future-threshold");
				result = false;
			}

			if (!chronologyOk) {
				super.state(context, false, "endMoment", "acme.validation.invention.dates.invalid-interval");
				result = false;
			}
		}

		return result;
	}
}
