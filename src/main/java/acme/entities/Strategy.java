
package acme.entities;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Moment;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidMoment.Constraint;
import acme.client.components.validation.ValidScore;
import acme.client.components.validation.ValidUrl;
import acme.client.helpers.MomentHelper;
import acme.client.helpers.SpringHelper;
import acme.client.helpers.StringHelper;
import acme.constraints.ValidHeader;
import acme.constraints.ValidText;
import acme.constraints.ValidTicker;
import acme.realms.Fundraiser;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Strategy extends AbstractEntity {

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
	@ValidScore
	private Double	expectedPercentage;

	// ---------------------------------------------------------
	// Derived attributes
	// ---------------------------------------------------------


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
	public Double getExpectedPercentage() {

		if (this.getId() == 0)
			return 0.0;

		StrategyRepository repository = SpringHelper.getBean(StrategyRepository.class);
		Double total = repository.computeExpectedPercentage(this.getId());

		this.expectedPercentage = total != null ? total : 0.0;

		return this.expectedPercentage;
	}

	// ---------------------------------------------------------
	// Relationships
	// ---------------------------------------------------------


	@OneToMany(mappedBy = "strategy")
	private Collection<Tactic>	tactics;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Fundraiser			fundraiser;
}
