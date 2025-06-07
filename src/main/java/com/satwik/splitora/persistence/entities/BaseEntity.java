package com.satwik.splitora.persistence.entities;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
public class BaseEntity {

    @Id
    @GenericGenerator(name = "uuid")
    @GeneratedValue(generator = "uuid")
    private UUID id;

    private UUID createdBy;

    @CreationTimestamp
    private LocalDateTime createdOn;

    private UUID modifiedBy;

    @UpdateTimestamp
    private LocalDateTime modifiedOn;

}
