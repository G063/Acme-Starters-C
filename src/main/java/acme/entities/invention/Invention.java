
package acme.entities.invention;

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
import acme.client.components.validation.ValidMoment.Constraint;
import acme.client.components.validation.ValidUrl;
import acme.constraints.ValidHeader;
import acme.constraints.ValidText;
import acme.constraints.ValidTicker;
import acme.constraints.invention.ValidInvention;
import acme.realms.Inventor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidInvention
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

	@Transient
	@Autowired
	InventionRepository			repository;


	@Mandatory
	@Valid
	@Transient
	public Double getMonthsActive() {
		if (this.startMoment == null || this.endMoment == null)
			return 0.0;
		long diffInMillies = Math.abs(this.endMoment.getTime() - this.startMoment.getTime());

		double days = (double) diffInMillies / (1000 * 60 * 60 * 24);

		double months = days / 30.44;

		return Math.round(months * 10.0) / 10.0;
	}

	@Mandatory
	@Valid
	@Transient
	public Money getCost() {
		Double totalAmount = this.repository.computeInventionAmount(this.getId());

		Double value = totalAmount != null ? totalAmount : 0.0;

		Double roundedValue = Math.round(value * 100.0) / 100.0;

		Money result = new Money();
		result.setAmount(roundedValue);
		result.setCurrency("EUR");
		return result;
	}


	@Mandatory
	@Valid
	@ManyToOne
	private Inventor inventor;

}
