package com.solid.soft.solid_soft_bank.model.dto;

import com.solid.soft.solid_soft_bank.model.enums.CardStatusType;
import com.solid.soft.solid_soft_bank.model.enums.CardType;

import java.time.Instant;

public class CardDTO extends BaseDTO{

    private String name;

    private String surname;

    private String cardNo;

    private String expiredDate;

    private String cvc;

    private Double amount;

    private String currency;

    private CardType cardType;

    private CardStatusType status;

    private Double creditLimit;

    private Double spendingLimit;

    private String country;

    private Integer fraudRiskScore;

    private Boolean otpRequired;

    private Instant lastTransactionTime;

    private Boolean subscriptionEnabled;

    private Double outstandingDebt;

    private Boolean scheduledPaymentEnabled;


    public CardDTO() {
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

    public CardType getCardType() {
        return cardType;
    }

    public void setCardType(final CardType cardType) {
        this.cardType = cardType;
    }

    public CardStatusType getStatus() {
        return status;
    }

    public void setStatus(final CardStatusType status) {
        this.status = status;
    }

    public Double getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(final Double creditLimit) {
        this.creditLimit = creditLimit;
    }

    public Double getSpendingLimit() {
        return spendingLimit;
    }

    public void setSpendingLimit(final Double spendingLimit) {
        this.spendingLimit = spendingLimit;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(final String country) {
        this.country = country;
    }

    public Integer getFraudRiskScore() {
        return fraudRiskScore;
    }

    public void setFraudRiskScore(final Integer fraudRiskScore) {
        this.fraudRiskScore = fraudRiskScore;
    }

    public Boolean getOtpRequired() {
        return otpRequired;
    }

    public void setOtpRequired(final Boolean otpRequired) {
        this.otpRequired = otpRequired;
    }

    public Instant getLastTransactionTime() {
        return lastTransactionTime;
    }

    public void setLastTransactionTime(final Instant lastTransactionTime) {
        this.lastTransactionTime = lastTransactionTime;
    }

    public Boolean getSubscriptionEnabled() {
        return subscriptionEnabled;
    }

    public void setSubscriptionEnabled(final Boolean subscriptionEnabled) {
        this.subscriptionEnabled = subscriptionEnabled;
    }

    public Double getOutstandingDebt() {
        return outstandingDebt;
    }

    public void setOutstandingDebt(final Double outstandingDebt) {
        this.outstandingDebt = outstandingDebt;
    }

    public Boolean getScheduledPaymentEnabled() {
        return scheduledPaymentEnabled;
    }

    public void setScheduledPaymentEnabled(final Boolean scheduledPaymentEnabled) {
        this.scheduledPaymentEnabled = scheduledPaymentEnabled;
    }

    @Override
    public String toString() {
        return "CardDTO{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", cardNo='" + cardNo + '\'' +
                ", expiredDate='" + expiredDate + '\'' +
                ", cvc='" + cvc + '\'' +
                ", amount=" + amount +
                ", currency='" + currency + '\'' +
                ", cardType=" + cardType +
                ", status=" + status +
                ", creditLimit=" + creditLimit +
                ", spendingLimit=" + spendingLimit +
                ", country='" + country + '\'' +
                ", fraudRiskScore=" + fraudRiskScore +
                ", otpRequired=" + otpRequired +
                ", lastTransactionTime=" + lastTransactionTime +
                ", subscriptionEnabled=" + subscriptionEnabled +
                ", outstandingDebt=" + outstandingDebt +
                ", scheduledPaymentEnabled=" + scheduledPaymentEnabled +
                '}';
    }
}
