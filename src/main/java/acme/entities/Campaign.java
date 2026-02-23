package acme.entities;

import javax.persistence.Transient;

import java.time.Duration;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidMoment.Constraint;
import acme.client.components.validation.ValidUrl;
import acme.client.helpers.MomentHelper;
import acme.client.helpers.SpringHelper;
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
	private Date startMoment;
	
	@Mandatory
	@ValidMoment(constraint = Constraint.ENFORCE_FUTURE)
	@Temporal(TemporalType.TIMESTAMP)
	private Date endMoment;
	
	
	@Optional
	@ValidUrl
	@Column
	private String moreInfo;
	
	@Mandatory
	@Valid
	@Column
	private Boolean draftMode;
	
	@Transient
	@Autowired
	private CampaignRepository repo;
	
	@Transient
	public Double getMonthsActive() {

	    if (this.startMoment == null || this.endMoment == null)
	        return 0.0;

	    Duration millis = MomentHelper.computeDuration(this.startMoment, this.endMoment);

	    double days = millis.getSeconds() / (60.0 * 60.0 * 24.0);

	    double months = days / 30.44;

	    return Math.round(months * 10.0) / 10.0;
	}
	
	@Transient
	public Double getEffort() {

	    Double totalEffort = this.repo.sumEffortByCampaignId(this.getId());

	    return totalEffort == null ? 0.0 : totalEffort;
	}
	
	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private SpokesPerson spokesperson;
}
