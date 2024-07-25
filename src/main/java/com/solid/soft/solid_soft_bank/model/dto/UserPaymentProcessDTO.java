package com.solid.soft.solid_soft_bank.model.dto;

public class UserPaymentProcessDTO {

    private Long id;

    private String name;

    private String surname;

    private String cardNo;

    private String expiredDate;

    private String cvc;

    private Double amount;

    private String currency;

    private String merchantName;

    private String bankTransactionCode;

    public UserPaymentProcessDTO() {
    }

    public UserPaymentProcessDTO(final String name, final String surname, final String cardNo, final String expiredDate, final String cvc,
                                 final Double amount, final String currency,
                                 final String merchantName, final String bankTransactionCode) {
        this.name = name;
        this.surname = surname;
        this.cardNo = cardNo;
        this.expiredDate = expiredDate;
        this.cvc = cvc;
        this.amount = amount;
        this.currency = currency;
        this.merchantName = merchantName;
        this.bankTransactionCode = bankTransactionCode;
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

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(final String merchantName) {
        this.merchantName = merchantName;
    }

    public String getBankTransactionCode() {
        return bankTransactionCode;
    }

    public void setBankTransactionCode(final String bankTransactionCode) {
        this.bankTransactionCode = bankTransactionCode;
    }
}
