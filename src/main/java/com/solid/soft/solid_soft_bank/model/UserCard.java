package com.solid.soft.solid_soft_bank.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_card")
public class UserCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "card_no")
    private String cardNo;

    @Column(name = "expire_date")
    private String expiredDate;

    @Column(name = "cvc")
    private String cvc;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "currency")
    private String currency;

    @Column(name = "merchant_name")
    private String merchantName;

    @Column(name = "bank_transaction_code")
    private String bankTransactionCode;

    public UserCard() {
    }

    public UserCard(final String name, final String surname, final String cardNo, final String expiredDate, final String cvc,
                    final Double amount, final String currency, String merchantName, String bankTransactionCode) {
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

    @Override
    public String toString() {
        return "UserCard{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", cardNo='" + cardNo + '\'' +
                ", expiredDate='" + expiredDate + '\'' +
                ", cvc='" + cvc + '\'' +
                ", amount=" + amount +
                ", currency='" + currency + '\'' +
                ", merchantName='" + merchantName + '\'' +
                ", bankTransactionCode='" + bankTransactionCode + '\'' +
                '}';
    }
}
