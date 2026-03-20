package acme.features.spokesperson.campaign;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.helpers.MessageHelper;
import acme.client.services.AbstractService;
import acme.entities.campaign.Campaign;
import acme.realms.Spokesperson;

@Service
public class SpokespersonCampaignListService extends AbstractService<Spokesperson, Campaign> {

	@Autowired
	private SpokespersonCampaignRepository repository;

	private Collection<Campaign> campaigns;
	
	@Override
	public void authorise() {
		super.setAuthorised(true);
	}

	@Override
	public void load() {
		int spokespersonId;

		spokespersonId = super.getRequest().getPrincipal().getActiveRealm().getId();
		this.campaigns = this.repository.findCampaignsBySpokespersonId(spokespersonId);

	}

	@Override
	public void unbind() {
		for (Campaign s : this.campaigns) {
			Tuple tuple;

			tuple = super.unbindObject(s, "ticker", "name", "effort");
			tuple.put("draftMode", this.getDraftModeLabel(s.getDraftMode()));
		}

	}

	private String getDraftModeLabel(final Boolean draftMode) {
		String key;

		key = Boolean.TRUE.equals(draftMode) ? "spokesperson.campaign.form.value.draft" : "spokesperson.campaign.form.value.published";

		return MessageHelper.getMessage(key);
	}
}
