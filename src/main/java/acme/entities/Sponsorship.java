
package acme.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Moment;
import acme.client.components.datatypes.Money;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidMoment.Constraint;
import acme.client.components.validation.ValidUrl;
import acme.client.helpers.MomentHelper;
import acme.client.helpers.SpringHelper;
import acme.client.helpers.StringHelper;
import acme.constraints.ValidHeader;
import acme.constraints.ValidText;
import acme.constraints.ValidTicker;
import acme.realms.Sponsor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
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
	@ValidMoment(constraint = Constraint.ENFORCE_FUTURE)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				startMoment;

	@Mandatory
	@ValidMoment(constraint = Constraint.ENFORCE_FUTURE)
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


	public void setStartMoment(final String startMoment) {
		assert !StringHelper.isBlank(startMoment);
		Date date = MomentHelper.parse(startMoment, "yyyy/MM/dd HH:mm");
		Moment moment = new Moment();
		moment.setTime(date.getTime());
		this.startMoment = moment;
	}

	public void setEndMoment(final String endMoment) {
		assert !StringHelper.isBlank(endMoment);
		Date date = MomentHelper.parse(endMoment, "yyyy/MM/dd HH:mm");
		Moment moment = new Moment();
		moment.setTime(date.getTime());
		this.endMoment = moment;
	}

	@Transient
	public Double getMonthsActive() {

		if (this.startMoment == null || this.endMoment == null)
			return 0.0;

		if (!MomentHelper.isBefore(this.startMoment, this.endMoment))
			return 0.0;

		long millis = this.endMoment.getTime() - this.startMoment.getTime();

		double days = millis / (1000.0 * 60.0 * 60.0 * 24.0);

		double months = days / 30.0;

		return Math.round(months * 10.0) / 10.0;
	}

	@Transient
	public Money getTotalMoney() {
		SponsorshipRepository repository = SpringHelper.getBean(SponsorshipRepository.class);
		Double totalAmount = repository.sumMoney(this.getId());

		Money totalMoney = new Money();
		totalMoney.setAmount(totalAmount);
		totalMoney.setCurrency("EUR");

		return totalMoney;
	}


	@Mandatory
	@Valid
	@ManyToOne
	private Sponsor sponsor;
}
