package com.solid.soft.solid_soft_bank.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "merchant")
public class MerchantEntity extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "web_site")
    private String webSite;

    @Column(name = "api_key")
    private String apiKey;

    @Column(name = "server_api_url")
    private String serverApiUrl;

    @Column(name = "callback_endpoint")
    private String callbackEndpoint;

    @Column(name = "success_redirect_url")
    private String successRedirectURL;

    @Column(name = "failed_redirect_url")
    private String failedRedirectURL;

    public MerchantEntity() {
    }

    public MerchantEntity(String name, String webSite, String serverApiUrl, String apiKey, String callbackEndpoint, String successRedirectURL, String failedRedirectURL) {
        this.name = name;
        this.webSite = webSite;
        this.serverApiUrl = serverApiUrl;
        this.apiKey = apiKey;
        this.callbackEndpoint = callbackEndpoint;
        this.successRedirectURL = successRedirectURL;
        this.failedRedirectURL = failedRedirectURL;
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

    @Override
    public String toString() {
        return "MerchantEntity{" +
                "name='" + name + '\'' +
                ", webSite='" + webSite + '\'' +
                ", serverApiUrl='" + serverApiUrl + '\'' +
                ", apiKey='" + apiKey + '\'' +
                ", callbackEndpoint='" + callbackEndpoint + '\'' +
                ", successRedirectURL='" + successRedirectURL + '\'' +
                ", failedRedirectURL='" + failedRedirectURL + '\'' +
                '}';
    }
}
