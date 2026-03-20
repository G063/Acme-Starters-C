
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

		status = this.sponsorship != null && Boolean.TRUE.equals(this.sponsorship.getDraftMode()) && this.sponsorship.getSponsor() != null && this.sponsorship.getSponsor().isPrincipal();

		super.setAuthorised(status);
	}

	@Override
	public void bind() {
		super.bindObject(this.sponsorship, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo");
	}

	@Override
	public void validate() {
		Date baseMoment = MomentHelper.getBaseMoment();
		Date endMoment;
		Date startMoment;
		Long donations;
		boolean allDonationsEUR;
		boolean validInterval;
		boolean validStartMoment;
		boolean validDonations;

		super.state(this.sponsorship != null, "*", "sponsor.sponsorship.error.not-found");
		if (this.sponsorship != null) {
			super.validateObject(this.sponsorship);

			donations = this.repository.countDonationsBySponsorshipId(this.sponsorship.getId());
			validDonations = donations != null && donations >= 1L;
			super.state(validDonations, "*", "acme.validation.sponsorship.no-donations.message");

			startMoment = this.sponsorship.getStartMoment();
			endMoment = this.sponsorship.getEndMoment();
			validStartMoment = startMoment != null && MomentHelper.isAfter(startMoment, baseMoment);
			super.state(validStartMoment, "*", "acme.validation.sponsorship.past-moment.message");

			validInterval = startMoment != null && endMoment != null && MomentHelper.isBefore(startMoment, endMoment);
			super.state(validInterval, "startMoment", "acme.validation.sponsorship.invalid-interval.message");
			super.state(validInterval, "endMoment", "acme.validation.sponsorship.invalid-interval.message");

			allDonationsEUR = this.repository.findDonationsBySponsorshipId(this.sponsorship.getId()).stream().allMatch(d -> "EUR".equals(d.getMoney().getCurrency()));
			super.state(allDonationsEUR, "*", "acme.validation.donation.money.currency.message");
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

		tuple = super.unbindObject(this.sponsorship, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo", "draftMode");

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
