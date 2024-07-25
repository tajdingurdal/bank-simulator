package com.solid.soft.solid_soft_bank.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "payment_transaction")
public class PaymentTransactionEntity extends BaseEntity {

    @Column(name = "bank_transaction_code")
    private String bankTransactionCode;

    @Column(name = "merchant_transaction_code")
    private String merchantTransactionCode;

    @Column(name = "merchant_id")
    private Long merchandId;

    @ManyToOne
    @JoinColumn(name = "merchant_id", updatable = false, insertable = false)
    private MerchantEntity merchantEntity;

    public PaymentTransactionEntity(final String bankTransactionCode,
                                    final String merchantTransactionCode,
                                    final Long merchandId,
                                    final MerchantEntity merchantEntity) {
        this.bankTransactionCode = bankTransactionCode;
        this.merchantTransactionCode = merchantTransactionCode;
        this.merchandId = merchandId;
        this.merchantEntity = merchantEntity;
    }

    public PaymentTransactionEntity() {

    }

    public String getBankTransactionCode() {
        return bankTransactionCode;
    }

    public void setBankTransactionCode(final String bankTransactionCode) {
        this.bankTransactionCode = bankTransactionCode;
    }

    public String getMerchantTransactionCode() {
        return merchantTransactionCode;
    }

    public void setMerchantTransactionCode(final String merchantTransactionCode) {
        this.merchantTransactionCode = merchantTransactionCode;
    }

    public Long getMerchandId() {
        return merchandId;
    }

    public void setMerchandId(final Long merchandId) {
        this.merchandId = merchandId;
    }

    public MerchantEntity getMerchantEntity() {
        return merchantEntity;
    }

    public void setMerchantEntity(final MerchantEntity merchantEntity) {
        this.merchantEntity = merchantEntity;
    }
}
