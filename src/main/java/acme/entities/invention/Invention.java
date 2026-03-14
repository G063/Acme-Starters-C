
package acme.entities.invention;

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
	@Autowired
	InventionRepository			repository;


	@Mandatory
	@Valid
	@Transient
	public Double getMonthsActive() {
		return MomentHelper.computeDifference(this.startMoment, this.endMoment, ChronoUnit.MONTHS);
	}

	@Mandatory
	@Valid
	@Transient
	public Money getCost() {

		Money result = new Money();
		result.setCurrency("EUR");

		if (this.repository == null || this.getId() == 0) {
			result.setAmount(0.0);
			return result;
		}

		Double totalAmount = this.repository.computeInventionAmount(this.getId());
		Double value = totalAmount != null ? totalAmount : 0.0;

		Double roundedValue = Math.round(value * 100.0) / 100.0;

		result.setAmount(roundedValue);
		return result;
	}


	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Inventor inventor;

}
