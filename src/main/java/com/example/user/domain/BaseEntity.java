package com.example.user.domain;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Data
@MappedSuperclass
public class BaseEntity {
    @CreatedDate
    @CreationTimestamp
    private LocalDateTime createdAt;

    @LastModifiedDate
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
