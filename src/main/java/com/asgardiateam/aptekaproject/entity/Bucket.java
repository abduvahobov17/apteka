package com.asgardiateam.aptekaproject.entity;

import com.asgardiateam.aptekaproject.audit.AuditingEntity;
import com.asgardiateam.aptekaproject.enums.BucketStatus;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.ArrayList;
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

    @Column(name = "amount")
    private Long amount;

    @Column(name = "bucket_status")
    @Enumerated(EnumType.STRING)
    private BucketStatus bucketStatus;

    @Builder.Default
    @OneToMany(mappedBy = "bucket", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<BucketProduct> bucketProducts = new ArrayList<>();

    @ManyToOne
    private User user;

    public void addBucketProducts(List<BucketProduct> bucketProducts) {
        this.bucketProducts.addAll(bucketProducts);
        bucketProducts.forEach(value -> value.setBucket(this));
    }
}
