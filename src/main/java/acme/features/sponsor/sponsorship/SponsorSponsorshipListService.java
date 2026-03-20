
package acme.features.sponsor.sponsorship;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.helpers.MessageHelper;
import acme.client.services.AbstractService;
import acme.entities.sponsorship.Sponsorship;
import acme.realms.Sponsor;

@Service
public class SponsorSponsorshipListService extends AbstractService<Sponsor, Sponsorship> {

	@Autowired
	private SponsorSponsorshipRepository	repository;

	private Collection<Sponsorship>			sponsorships;


	@Override
	public void load() {
		int sponsorId;

		sponsorId = super.getRequest().getPrincipal().getActiveRealm().getId();
		this.sponsorships = this.repository.findSponsorshipsBySponsorId(sponsorId);
	}

	@Override
	public void authorise() {
		super.setAuthorised(this.getRequest().getPrincipal().hasRealmOfType(Sponsor.class));
	}

	@Override
	public void unbind() {
		for (Sponsorship s : this.sponsorships) {
			Tuple tuple;

			tuple = super.unbindObject(s, "ticker", "name", "startMoment", "endMoment");
			tuple.put("draftMode", this.getDraftModeLabel(s.getDraftMode()));
		}

	}

	private String getDraftModeLabel(final Boolean draftMode) {
		String key;

		key = Boolean.TRUE.equals(draftMode) ? "sponsor.sponsorship.form.value.draft" : "sponsor.sponsorship.form.value.published";

		return MessageHelper.getMessage(key);
	}
}
