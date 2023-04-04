package com.asgardiateam.aptekaproject.entity;

import com.asgardiateam.aptekaproject.audit.AuditingEntity;
import com.asgardiateam.aptekaproject.enums.State;
import com.asgardiateam.aptekaproject.enums.Status;
import com.asgardiateam.aptekaproject.enums.UnitType;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "product")
public class Product extends AuditingEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "amount")
    private Long amount;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private Long price;

    @Column(name = "supplier")
    private String supplier;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "unit_type")
    @Enumerated(EnumType.STRING)
    private UnitType unitType;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    @Builder.Default
        private State state = State.ALIVE;

    public Product() {
        this.state = State.ALIVE;
    }
}
