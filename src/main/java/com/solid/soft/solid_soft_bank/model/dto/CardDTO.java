package com.solid.soft.solid_soft_bank.model.dto;

public class CardDTO {

    private Long id;

    private String name;

    private String surname;

    private String cardNo;

    private String expiredDate;

    private String cvc;

    private Double amount;

    private String currency;


    public CardDTO() {
    }

    public CardDTO(final Long id,
                   final String name,
                   final String surname,
                   final String cardNo,
                   final String expiredDate,
                   final String cvc,
                   final Double amount,
                   final String currency) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.cardNo = cardNo;
        this.expiredDate = expiredDate;
        this.cvc = cvc;
        this.amount = amount;
        this.currency = currency;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(final String surname) {
        this.surname = surname;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(final String cardNo) {
        this.cardNo = cardNo;
    }

    public String getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(final String expiredDate) {
        this.expiredDate = expiredDate;
    }

    public String getCvc() {
        return cvc;
    }

    public void setCvc(final String cvc) {
        this.cvc = cvc;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(final Double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(final String currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "CardDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", cardNo='" + cardNo + '\'' +
                ", expiredDate='" + expiredDate + '\'' +
                ", cvc='" + cvc + '\'' +
                ", amount=" + amount +
                ", currency='" + currency + '\'' +
                '}';
    }
}