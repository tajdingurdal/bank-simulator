package com.solid.soft.solid_soft_bank.model.dto;

public class MerchantDTO extends BaseDTO{

    private String name;
    private String webSite;
    private String apiKey;

    public MerchantDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(final String webSite) {
        this.webSite = webSite;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(final String apiKey) {
        this.apiKey = apiKey;
    }
}
