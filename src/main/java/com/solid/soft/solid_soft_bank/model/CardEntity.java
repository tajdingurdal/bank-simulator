package com.solid.soft.solid_soft_bank.model;

import com.solid.soft.solid_soft_bank.model.enums.CardStatusType;
import com.solid.soft.solid_soft_bank.model.enums.CardType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "card")
public class CardEntity extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "surname", nullable = false)
    private String surname;

    @Column(name = "card_no", unique = true, nullable = false)
    private String cardNo;

    @Column(name = "expire_date", nullable = false)
    private String expiredDate;

    @Column(name = "cvc", nullable = false)
    private String cvc;

    @Column(name = "balance", nullable = false)
    private Double balance;

    @Column(name = "currency", nullable = false)
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(name = "card_type", nullable = false)
    private CardType cardType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CardStatusType status;

    @Column(name = "credit_limit")
    private Double creditLimit;

    @Column(name = "spending_limit")
    private Double spendingLimit;

    @Column(name = "country", nullable = false)
    private String country;

    @Column(name = "fraud_risk_score", nullable = false)
    private Integer fraudRiskScore;

    @Column(name = "otp_required")
    private Boolean otpRequired;

    @Column(name = "last_transaction_time", nullable = false)
    private Instant lastTransactionTime;

    @Column(name = "subscription_enabled", nullable = false)
    private Boolean subscriptionEnabled;

    @Column(name = "outstanding_debt")
    private Double outstandingDebt;

    @Column(name = "scheduled_payment_enabled")
    private Boolean scheduledPaymentEnabled;

    public CardEntity() {
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
}
