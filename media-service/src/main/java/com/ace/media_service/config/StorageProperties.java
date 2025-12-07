package com.ace.media_service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "media.storage")
public class StorageProperties {

    /**
     * Dossier racine où stocker les fichiers.
     * Correspond à media.storage.location dans la config.
     */
    private String location = "uploads";

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}

