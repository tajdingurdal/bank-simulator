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

    public MerchantEntity() {
    }

    public MerchantEntity(final String name, final String webSite, final String apiKey) {
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

    @Override
    public String toString() {
        return "MerchantEntity{" +
                ", name='" + name + '\'' +
                ", webSite='" + webSite + '\'' +
                ", apiKey='" + apiKey + '\'' +
                '}';
    }
}
