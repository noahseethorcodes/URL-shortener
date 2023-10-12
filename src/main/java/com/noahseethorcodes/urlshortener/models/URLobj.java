package com.noahseethorcodes.urlshortener.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

@Entity // This tells Hibernate to make a table out of this class
public class URLobj {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotBlank(message = "valid target_url must be provided")
    @URL
    private String targetURL;
    private String key;
    private String adminKey;
    private Boolean active;
    private Integer clicks;

    public URLobj() {
    }

    public URLobj(String targetURL, String key, String adminKey, Boolean active, Integer clicks) {

        this.targetURL = targetURL;
        this.key = key;
        this.adminKey = adminKey;
        this.active = active;
        this.clicks = clicks;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTargetURL() {
        return this.targetURL;
    }

    public void setTargetURL(String targetURL) {
        this.targetURL = targetURL;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getAdminKey() {
        return this.adminKey;
    }

    public void setAdminKey(String adminKey) {
        this.adminKey = adminKey;
    }

    public Boolean isActive() {
        return this.active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Integer getClicks() {
        return this.clicks;
    }

    public void setClicks(Integer clicks) {
        this.clicks = clicks;
    }
}
