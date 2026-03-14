
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

		// 1. Validation of Ticker (Must be unique if present)
		if (value.getTicker() != null) {
			final Invention inv = this.inventionRepo.findInventionByTicker(value.getTicker());
			final boolean isUnique = inv == null || inv.getId() == id;
			if (!isUnique) {
				super.state(context, false, "ticker", "acme.validation.invention.ticker.non-unique");
				result = false;
			}
		}

		// 2. Inventions must have at least one part to be published and must already exist
		if (published)
			if (id != 0) {
				final Collection<Part> linkedParts = this.inventionRepo.findPartsByInventionId(id);
				final boolean containsItems = linkedParts != null && !linkedParts.isEmpty();
				if (!containsItems) {
					super.state(context, false, "draftMode", "acme.validation.invention.parts.message");
					result = false;
				}
			} else {
				// Cannot publish an invention that has not been created yet
				super.state(context, false, "draftMode", "acme.validation.invention.parts.message");
				result = false;
			}

		// 3. startMoment/endMoment validation
		if (value.getStartMoment() != null && value.getEndMoment() != null) {
			// Referencia: 01/01/2025 (según tu application.properties)
			final Date reference = MomentHelper.getBaseMoment();

			// REGLA 1: El fin SIEMPRE debe ser después del inicio
			final boolean chronologyOk = value.getEndMoment().after(value.getStartMoment());

			// REGLA 2: El inicio SIEMPRE debe ser futuro respecto a la referencia (incluso en borrador)
			final boolean futureOk = value.getStartMoment().after(reference);

			if (!chronologyOk || !futureOk) {
				super.state(context, false, "startMoment", "acme.validation.invention.dates.message");
				super.state(context, false, "endMoment", "acme.validation.invention.dates.message");
				result = false;
			}
		}

		return result;
	}
}
