package com.solid.soft.solid_soft_bank.utils;

public class ResponseMessages {
    public static final String SUBSCRIBE_SUCCESS    = "It have been subscribed successfully";
    public static final String AUTHENTICATE_FAILED  = "Authentication failed! Please check your credentials and try again.";
    public static final String AUTHENTICATE_SUCCESS = "Authentication successful! You are now logged in.";


    public static String nullParameterMessage(String merchantTransactionCode, String apiKey, String amount, String currency) {
        return String.format("One or more parameters are null. merchantTransactionCode: %s, apiKey: %s, amount: %s, currency: %s",
                             merchantTransactionCode, apiKey, amount, currency);
    }

    public static String invalidApiKeyMessage(String apiKey) {
        return String.format("API key %s is not valid.", apiKey);
    }

    public static String invalidTransactionCodeLengthMessage(String merchantTransactionCode) {
        return String.format("Merchant transaction code %s is not valid. Length should be 16, but was %d.",
                             merchantTransactionCode, merchantTransactionCode.length());
    }

    public static String invalidAmountMessage(long amountLong) {
        return String.format("Amount %d is not valid. It should be between 0 and 1000000.", amountLong);
    }

    public static String invalidCurrencyMessage(String currency) {
        return String.format("Currency %s is not valid.", currency);
    }

    public static String transactionCodeAlreadySubscribedMessage(String merchantTransactionCode) {
        return String.format("Merchant transaction code %s is already subscribed.", merchantTransactionCode);
    }

}
