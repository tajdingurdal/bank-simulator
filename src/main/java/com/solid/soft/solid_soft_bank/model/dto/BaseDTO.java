package com.solid.soft.solid_soft_bank.model.dto;

import java.time.Instant;

public class BaseDTO {

    private Long   id;
    private Instant createdDate;
    private Instant modifiedDate;

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
