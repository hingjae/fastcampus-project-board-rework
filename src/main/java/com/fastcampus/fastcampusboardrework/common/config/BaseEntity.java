package com.fastcampus.fastcampusboardrework.common.config;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class BaseEntity {
    @CreatedDate
    protected LocalDateTime createdAt;

    @CreatedBy
    protected String createdBy;

    @LastModifiedDate
    protected LocalDateTime modifiedAt;

    @LastModifiedBy
    protected String modifiedBy;
}
