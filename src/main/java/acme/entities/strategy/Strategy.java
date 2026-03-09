
package acme.entities.strategy;

import java.time.Duration;
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
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidMoment.Constraint;
import acme.client.components.validation.ValidUrl;
import acme.client.helpers.MomentHelper;
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

	// ---------------------------------------------------------
	// Derived attributes
	// ---------------------------------------------------------


	@Transient
	public Double getMonthsActive() {

		if (this.startMoment == null || this.endMoment == null)
			return 0.0;
		Duration diffInMillies = MomentHelper.computeDuration(this.startMoment, this.endMoment);
		double days = diffInMillies.getSeconds() / (60. * 60 * 24);
		double months = days / 30.44;

		return Math.round(months * 10.0) / 10.0;
	}


	@Transient
	@Autowired
	private StrategyRepository repository;


	@Transient
	public Double getExpectedPercentage() {

		if (this.getId() == 0)
			return 0.0;

		Double total = this.repository.computeExpectedPercentage(this.getId());

		return total != null ? total : 0.0;
	}

	// ---------------------------------------------------------
	// Relationships
	// ---------------------------------------------------------


	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Fundraiser fundraiser;
}
