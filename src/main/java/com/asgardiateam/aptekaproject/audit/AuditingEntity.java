package com.asgardiateam.aptekaproject.audit;

import com.asgardiateam.aptekaproject.common.ThreadLocalSingleton;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@SuperBuilder
@MappedSuperclass
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditingEntity implements Serializable {

    @CreatedBy
    @Column(name = "created_by", length = 50, updatable = false)
    @JsonIgnore
    private String createdBy;

    @CreatedDate
    @Column(name = "created_date", updatable = false)
    @JsonIgnore
    private Instant createdDate;

    @LastModifiedBy
    @Column(name = "last_modified_by", length = 50)
    @JsonIgnore
    private String lastModifiedBy;

    @LastModifiedDate
    @Column(name = "last_modified_date")
    @JsonIgnore
    private Instant lastModifiedDate;

    @PrePersist
    protected void onCreate() {
        createdDate = Instant.ofEpochMilli(System.currentTimeMillis());
        createdBy = ThreadLocalSingleton.getUser().getLogin();
        lastModifiedDate = Instant.ofEpochMilli(System.currentTimeMillis());
        lastModifiedBy = ThreadLocalSingleton.getUser().getLogin();
    }

    @PreUpdate
    protected void onUpdate() {
        lastModifiedDate = Instant.ofEpochMilli(System.currentTimeMillis());
        lastModifiedBy = ThreadLocalSingleton.getUser().getLogin();
    }
}
