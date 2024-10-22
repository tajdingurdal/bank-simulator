package com.solid.soft.solid_soft_bank.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "card")
public class CardEntity extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "card_no", unique = true)
    private String cardNo;

    @Column(name = "expire_date")
    private String expiredDate;

    @Column(name = "cvc")
    private String cvc;

    @Column(name = "balance")
    private Double balance;

    @Column(name = "currency")
    private String currency;

    public CardEntity() {
    }

    public CardEntity(final String name, final String surname, final String cardNo, final String expiredDate, final String cvc,
                      final Double balance, final String currency) {
        this.name = name;
        this.surname = surname;
        this.cardNo = cardNo;
        this.expiredDate = expiredDate;
        this.cvc = cvc;
        this.balance = balance;
        this.currency = currency;
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

    public Double getBalance() {
        return balance;
    }

    public void setBalance(final Double amount) {
        this.balance = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(final String currency) {
        this.currency = currency;
    }
}
