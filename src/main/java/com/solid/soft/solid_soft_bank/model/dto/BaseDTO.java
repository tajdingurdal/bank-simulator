package com.solid.soft.solid_soft_bank.model.dto;

import java.time.Instant;

public class BaseDTO {

    private Long   id;
    private Instant createdDate;
    private Instant modifiedDate;

    public BaseDTO() {
    }

    public BaseDTO(final Long id, final Instant createdDate, final Instant modifiedDate) {
        this.id = id;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(final Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(final Instant modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
}
