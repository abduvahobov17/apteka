package com.asgardiateam.aptekaproject.entity;

import com.asgardiateam.aptekaproject.audit.AuditingEntity;
import com.asgardiateam.aptekaproject.enums.BucketProductStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties("bucket")
@Entity
@Table(name = "bucket_product")
public class BucketProduct extends AuditingEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "amount")
    private Long amount;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private BucketProductStatus status;

    @ManyToOne
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bucket_id")
    private Bucket bucket;
}
