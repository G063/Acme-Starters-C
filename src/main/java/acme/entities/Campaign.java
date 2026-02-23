package acme.entities;

import javax.persistence.Transient;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Moment;
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
import acme.realms.SpokesPerson;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Campaign extends AbstractEntity{
	
	private static final long serialVersionUID = 1L;

	@Mandatory
	@ValidTicker
	@Column(unique = true)
	private String ticker;
	
	@Mandatory
	@ValidHeader
	@Column
	private String name;
	
	@Mandatory
	@ValidText
	@Column
	private String description;
	
	@Mandatory
	@ValidMoment(constraint = Constraint.ENFORCE_FUTURE)
	@Temporal(TemporalType.TIMESTAMP)
	private Moment startMoment;
	
	@Mandatory
	@ValidMoment(constraint = Constraint.ENFORCE_FUTURE)
	@Temporal(TemporalType.TIMESTAMP)
	private Moment endMoment;
	
	
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
	private String moreInfo;
	
	@Mandatory
	@Valid
	@Column
	private Boolean draftMode;
	
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
	public Double getEffort() {

	    CampaignRepository repository = SpringHelper.getBean(CampaignRepository.class);
	    Double totalEffort = repository.sumEffortByCampaignId(this.getId());

	    return totalEffort == null ? 0.0 : totalEffort;
	}
	
	@Mandatory
	@Valid
	@ManyToOne
	private SpokesPerson spokesperson;
}
