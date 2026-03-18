
package acme.features.sponsor.sponsorship;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.entities.sponsorship.Sponsorship;
import acme.realms.Sponsor;

@Service
public class SponsorSponsorshipPublishService extends AbstractService<Sponsor, Sponsorship> {

	@Autowired
	private SponsorSponsorshipRepository	repository;

	private Sponsorship						sponsorship;


	@Override
	public void load() {
		int id;

		id = super.getRequest().getData("id", int.class);
		this.sponsorship = this.repository.findSponsorshipById(id);
	}

	@Override
	public void authorise() {
		boolean status;
		boolean isSponsor;
		boolean isOwner;
		boolean isDraft;
		int sponsorId;

		isSponsor = this.getRequest().getPrincipal().hasRealmOfType(Sponsor.class);
		if (!isSponsor || this.sponsorship == null)
			status = false;
		else {
			sponsorId = this.getRequest().getPrincipal().getActiveRealm().getId();
			isOwner = this.sponsorship.getSponsor() != null && this.sponsorship.getSponsor().getId() == sponsorId;
			isDraft = Boolean.TRUE.equals(this.sponsorship.getDraftMode());
			status = isOwner && isDraft;
		}

		super.setAuthorised(status);
	}

	@Override
	public void bind() {
		super.bindObject(this.sponsorship, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo");
	}

	@Override
	public void validate() {
		Date baseMoment = MomentHelper.getBaseMoment();
		Date startMoment;
		boolean validStartMoment;

		super.state(this.sponsorship != null, "*", "sponsor.sponsorship.error.not-found");
		if (this.sponsorship != null) {
			startMoment = this.sponsorship.getStartMoment();
			validStartMoment = startMoment != null && MomentHelper.isAfter(startMoment, baseMoment);
			super.state(validStartMoment, "*", "acme.validation.sponsorship.past-moment.message");
			super.validateObject(this.sponsorship);
		}

	}

	@Override
	public void execute() {
		this.sponsorship.setDraftMode(false);
		this.repository.save(this.sponsorship);

		super.getResponse().setView("redirect:/sponsor/sponsorship/show?id=" + this.sponsorship.getId());
	}

	@Override
	public void unbind() {
		Tuple tuple;

		tuple = super.unbindObject(this.sponsorship, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo");

		tuple.put("monthsActive", this.sponsorship.getMonthsActive());
		tuple.put("totalMoney", this.sponsorship.getTotalMoney());
		tuple.put("draftModes", this.getDraftModeChoices(this.sponsorship.getDraftMode()));
	}

	private SelectChoices getDraftModeChoices(final Boolean draftMode) {
		SelectChoices choices;

		choices = this.newObject(SelectChoices.class);
		choices.add("true", "sponsor.sponsorship.form.value.draft", Boolean.TRUE.equals(draftMode));
		choices.add("false", "sponsor.sponsorship.form.value.published", Boolean.FALSE.equals(draftMode));

		return choices;
	}
}
