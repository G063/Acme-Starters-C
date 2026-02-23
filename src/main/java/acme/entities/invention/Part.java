
package acme.entities.invention;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidMoney;
import acme.constraints.ValidHeader;
import acme.constraints.ValidText;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Part extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidHeader
	@Column
	private String				name;

	@Mandatory
	@ValidText
	@Column
	private String				description;

	@Mandatory
	@ValidMoney(min = 0.)
	@Column
	private Money				cost;


	public void setCost(final Money cost) {
		if (cost != null && !cost.getCurrency().equals("EUR"))
			throw new IllegalArgumentException("Only Euros are accepted");
		this.cost = cost;
	}


	@Mandatory
	@Valid
	@Column
	private PartKind	kind;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Invention	invention;

}
