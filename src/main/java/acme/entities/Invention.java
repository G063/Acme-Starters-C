
package acme.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
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
import acme.realms.Inventor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Invention extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidTicker
	@Column(unique = true)
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
	private Moment				startMoment;

	@Mandatory
	@ValidMoment(constraint = Constraint.ENFORCE_FUTURE)
	private Moment				endMoment;


	public void setStartMoment(final String startMoment) {
		assert !StringHelper.isBlank(startMoment);
		java.util.Date date = MomentHelper.parse(startMoment, "yyyy/MM/dd HH:mm");
		Moment moment = new Moment();
		moment.setTime(date.getTime());
		this.startMoment = moment;
	}

	public void setEndMoment(final String endMoment) {
		assert !StringHelper.isBlank(endMoment);
		java.util.Date date = MomentHelper.parse(endMoment, "yyyy/MM/dd HH:mm");
		Moment moment = new Moment();
		moment.setTime(date.getTime());
		this.endMoment = moment;
	}


	@Optional
	@ValidUrl
	@Column
	private String	moreInfo;

	@Mandatory
	@Valid
	@Column
	private Boolean	draftMode;


	@Transient
	public Double getMonthsActive() {
		if (this.startMoment == null || this.endMoment == null)
			return 0.0;
		long diffInMillies = Math.abs(this.endMoment.getTime() - this.startMoment.getTime());

		double days = (double) diffInMillies / (1000 * 60 * 60 * 24);

		double months = days / 30.44;

		return Math.round(months * 10.0) / 10.0;
	}

	@Transient
	public Money getCost() {
		InventionRepository repository = SpringHelper.getBean(InventionRepository.class);
		Double totalAmount = repository.computeInventionAmount(this.getId());

		Money result = new Money();
		result.setAmount(totalAmount != null ? totalAmount : 0.0);
		result.setCurrency("EUR");

		return result;
	}


	@Mandatory
	@Valid
	@ManyToOne
	private Inventor inventor;

}
