package com.sales_scout.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

//import com.sales_scout.Auth.SecurityUtils;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @CreatedBy
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", referencedColumnName = "id", updatable = false, nullable = true)
    @JsonIgnore
    private UserEntity createdBy;

    @LastModifiedBy
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by", referencedColumnName = "id", nullable = true)
    @JsonIgnore
    private UserEntity updatedBy;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
//        createdBy = SecurityUtils.getCurrentUser(); // ✅ Automatically set creator
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
//        updatedBy = SecurityUtils.getCurrentUser(); // ✅ Automatically set updater
    }

    public boolean isDeleted() {
        return deletedAt != null;
    }

    public void softDelete() {
        this.deletedAt = LocalDateTime.now();
    }
}
