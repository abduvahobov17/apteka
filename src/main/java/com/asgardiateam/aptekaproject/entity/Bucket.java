package com.asgardiateam.aptekaproject.entity;

import com.asgardiateam.aptekaproject.audit.AuditingEntity;
import com.asgardiateam.aptekaproject.enums.BucketStatus;
import com.asgardiateam.aptekaproject.enums.PaymentType;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "bucket")
public class Bucket extends AuditingEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "bucket_status")
    @Enumerated(EnumType.STRING)
    private BucketStatus bucketStatus;

    @Column(name = "payment_type")
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @Column(name = "overall_amount")
    private Long overallAmount;

    @Column(name = "lon")
    private Double lon;

    @Column(name = "lat")
    private Double lat;

    @Builder.Default
    @OneToMany(mappedBy = "bucket", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<BucketProduct> bucketProducts = new ArrayList<>();

    @ManyToOne
    private User user;

    public void addBucketProducts(List<BucketProduct> bucketProducts) {
        this.bucketProducts.addAll(bucketProducts);
        bucketProducts.forEach(value -> value.setBucket(this));
    }

    public Bucket() {
        this.overallAmount = 0L;
    }
}
