
package acme.entities.sponsorship;

import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidUrl;
import acme.client.helpers.MomentHelper;
import acme.constraints.ValidHeader;
import acme.constraints.ValidText;
import acme.constraints.ValidTicker;
import acme.constraints.sponsorship.ValidSponsorship;
import acme.realms.Sponsor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidSponsorship
public class Sponsorship extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidTicker
	@Column
	private String				ticker;

	@Mandatory
	@ValidHeader
	@Column
	private String				name;

	@Mandatory
	@ValidText
	@Column
	private String				description;

	@Mandatory
	@ValidMoment
	@Temporal(TemporalType.TIMESTAMP)
	private Date				startMoment;

	@Mandatory
	@ValidMoment
	@Temporal(TemporalType.TIMESTAMP)
	private Date				endMoment;

	@Optional
	@ValidUrl
	@Column
	private String				moreInfo;

	@Mandatory
	@Valid
	@Column
	private Boolean				draftMode;


	@Transient
	public Double getMonthsActive() {
		if (this.startMoment == null || this.endMoment == null)
			return 0.0;

		return MomentHelper.computeDifference(this.startMoment, this.endMoment, ChronoUnit.MONTHS);
	}


	@Transient
	@Autowired
	private SponsorshipRepository repo;


	@Transient
	public Money getTotalMoney() {

		Money totalMoney = new Money();
		totalMoney.setCurrency("EUR");
		if (this.repo == null || this.getId() == 0) {
			totalMoney.setAmount(0.0);
			return totalMoney;
		}
		Double totalAmount = this.repo.sumMoney(this.getId());
		Double value = totalAmount != null ? totalAmount : 0.0;

		Double roundedValue = Math.round(value * 100.0) / 100.0;

		totalMoney.setAmount(roundedValue);

		return totalMoney;
	}


	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Sponsor sponsor;
}
