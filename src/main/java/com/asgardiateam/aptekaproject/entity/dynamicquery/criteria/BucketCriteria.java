package com.asgardiateam.aptekaproject.entity.dynamicquery.criteria;

import com.asgardiateam.aptekaproject.common.deserializer.BucketStatusDeserializer;
import com.asgardiateam.aptekaproject.enums.BucketStatus;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@SuperBuilder
public class BucketCriteria {

    private Instant fromDate;

    private Instant toDate;

    @JsonDeserialize(using = BucketStatusDeserializer.class)
    private BucketStatus bucketStatus;

    public BucketCriteria() {
        this.fromDate = Instant.ofEpochMilli(0L);
    }

    public void setFromDate(Long fromDate) {
        this.fromDate = Instant.ofEpochMilli(fromDate);
    }

    public void setToDate(Long toDate) {
        this.toDate = Instant.ofEpochMilli(toDate);
    }
}
