package com.solid.soft.solid_soft_bank.model.dto;

public class BaseDTO {

    private Long   id;
    public BaseDTO() {
    }

    public BaseDTO(final Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

}
