package com.solid.soft.solid_soft_bank.model.dto;

public class MerchantDTO {

    private Long   id;
    private String name;
    private String webSite;
    private String apiKey;

    public MerchantDTO() {
    }

    public MerchantDTO(Long id, final String name, final String webSite, final String apiKey) {
        this.id = id;
        this.name = name;
        this.webSite = webSite;
        this.apiKey = apiKey;
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

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }
}
