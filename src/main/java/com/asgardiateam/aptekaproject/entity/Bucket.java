package com.asgardiateam.aptekaproject.entity;

import com.asgardiateam.aptekaproject.audit.AuditingEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "bucket")
public class Bucket extends AuditingEntity {

    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Product> products;

    @ManyToOne
    private User user;

}
