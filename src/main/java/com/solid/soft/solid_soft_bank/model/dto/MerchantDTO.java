package com.solid.soft.solid_soft_bank.model.dto;

public class MerchantDTO extends BaseDTO{

    private String name;
    private String webSite;
    private String serverApiUrl;
    private String apiKey;
    private String callbackEndpoint;
    private String successRedirectURL;
    private String failedRedirectURL;

    public MerchantDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

    public String getServerApiUrl() {
        return serverApiUrl;
    }

    public void setServerApiUrl(String serverApiUrl) {
        this.serverApiUrl = serverApiUrl;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getCallbackEndpoint() {
        return callbackEndpoint;
    }

    public void setCallbackEndpoint(String callbackEndpoint) {
        this.callbackEndpoint = callbackEndpoint;
    }

    public String getSuccessRedirectURL() {
        return successRedirectURL;
    }

    public void setSuccessRedirectURL(String successRedirectURL) {
        this.successRedirectURL = successRedirectURL;
    }

    public String getFailedRedirectURL() {
        return failedRedirectURL;
    }

    public void setFailedRedirectURL(String failedRedirectURL) {
        this.failedRedirectURL = failedRedirectURL;
    }
}
