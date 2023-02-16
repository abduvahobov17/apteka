package com.asgardiateam.aptekaproject.entity;

import com.asgardiateam.aptekaproject.audit.AuditingEntity;
import com.asgardiateam.aptekaproject.enums.UnitType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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

    @Column(name = "unit_type")
    @Enumerated(EnumType.STRING)
    private UnitType unitType;
}
