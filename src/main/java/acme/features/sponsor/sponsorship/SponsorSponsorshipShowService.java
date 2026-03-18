
package acme.features.sponsor.sponsorship;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractService;
import acme.entities.sponsorship.Sponsorship;
import acme.realms.Sponsor;

@Service
public class SponsorSponsorshipShowService extends AbstractService<Sponsor, Sponsorship> {

	@Autowired
	private SponsorSponsorshipRepository	repository;

	private Sponsorship						sponsorship;


	@Override
	public void load() {
		int id;

		id = super.getRequest().getData("id", int.class);
		this.sponsorship = (Integer) id != null ? this.repository.findSponsorshipById(id) : null;
	}

	@Override
	public void authorise() {
		int sponsorId;
		boolean isOwner;
		boolean exists;

		exists = this.sponsorship != null;

		if (this.getRequest().getPrincipal() == null || !this.getRequest().getPrincipal().hasRealmOfType(Sponsor.class))
			isOwner = false;
		else {
			sponsorId = this.getRequest().getPrincipal().getActiveRealm().getId();

			isOwner = this.sponsorship != null && this.sponsorship.getSponsor() != null && this.sponsorship.getSponsor().getId() == sponsorId;

		}
		super.setAuthorised(isOwner && exists);
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
