package com.solid.soft.solid_soft_bank.utils;

import com.solid.soft.solid_soft_bank.model.enums.PaymentTransactionType;

public class ResponseMessages {
    public static final String SUBSCRIBE_SUCCESS = "It have been subscribed successfully";
    public static final String AUTHENTICATE_FAILED = "Authentication failed! Please check your credentials and try again.";
    public static final String AUTHENTICATE_SUCCESS = "Authentication successful! You are now logged in.";


    public static String nullParameterMessage(String merchantTransactionCode, String apiKey, Double amount, String currency) {
        return String.format("One or more parameters are null. merchantTransactionCode: %s, apiKey: %s, amount: %f, currency: %s",
                merchantTransactionCode, apiKey, amount, currency);
    }

    public static String invalidApiKeyMessage(String apiKey) {
        return String.format("API key %s is not valid.", apiKey);
    }

    public static String invalidTransactionCodeLengthMessage(String merchantTransactionCode) {
        return String.format("Merchant transaction code %s is not valid. Length should be 16, but was %d.",
                merchantTransactionCode, merchantTransactionCode.length());
    }

    public static String invalidAmountMessage(double amount) {
        return String.format("Amount %f is not valid. It should be between 0 and 1000000.", amount);
    }

    public static String invalidCurrencyMessage(String currency) {
        return String.format("Currency %s is not valid.", currency);
    }

    public static String transactionCodeAlreadySubscribedMessage(String merchantTransactionCode) {
        return String.format("Merchant transaction code %s is already subscribed.", merchantTransactionCode);
    }

    public static String paymentTransactionEntryNotFound(final String val, final PaymentTransactionType type) {
        return String.format("Payment Transaction Entry does not found by type %s and this value %s.", type.name(), val);
    }

    public static String merchantNotFoundByApiKey(final String apiKey) {
        return String.format("Merchant does not found by this %s API Key. You should register as Merchant first.", apiKey);
    }

    public static String bankAndMerchantAndApikeyAreNotValidCombinationError(final String merchantTransactionCode, final String bankTransactionCode) {
        return String.format("Merchant Transaction Code %s And Bank Transaction Code %s And API Key should be a valid combination.", merchantTransactionCode, bankTransactionCode);
    }
}
