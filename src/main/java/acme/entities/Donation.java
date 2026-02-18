
package acme.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import org.dom4j.tree.AbstractEntity;

import acme.client.components.datatypes.Money;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidMoney;
import acme.constraints.ValidHeader;
import acme.constraints.ValidText;
import acme.datatypes.DonationKind;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Donation extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidHeader
	@Column
	private String				name;

	@Mandatory
	@ValidText
	@Column

	private String				notes;

	@Mandatory
	@ValidMoney(min = 0.)
	@Column
	private Money				money;


	public void setCost(final Money money) {
		if (money != null && !money.getCurrency().equals("EUR"))
			throw new IllegalArgumentException("Only Euros are accepted");
		this.money = money;
	}


	@Mandatory
	@Valid
	@Column
	private DonationKind	kind;

	@Mandatory
	@Valid
	@ManyToOne
	private Sponsorship		sponsorship;

}
