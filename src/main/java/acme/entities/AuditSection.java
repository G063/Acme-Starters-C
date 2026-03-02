package acme.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidNumber;
import acme.constraints.ValidHeader;
import acme.constraints.ValidText;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class AuditSection extends AbstractEntity {

    // Serialisation version --------------------------------------------------

    private static final long serialVersionUID = 1L;

    // Attributes -------------------------------------------------------------

    @Mandatory
    @ValidHeader
    @Column
    private String name;

    @Mandatory
    @ValidText
    @Column
    private String notes;

    @Mandatory
    @ValidNumber(min = 1)
    @Column
    private Integer hours;

    @Mandatory
    @Valid
    @Enumerated(EnumType.STRING)
    @Column
    private SectionKind kind;

    // Relationships ----------------------------------------------------------

    @Mandatory
    @Valid
    @ManyToOne
    private AuditReport auditReport;

}
