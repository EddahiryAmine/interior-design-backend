package com.ace.stylecatalogservice.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "styles")
public class Style {

    @Id
    private String id;

    private String code;

    private String name;

    private String description;

    private List<String> keywords;

    public Style() {
    }

    public Style(String code, String name, String description, List<String> keywords) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.keywords = keywords;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) { this.id = id; }

    public String getCode() {
        return code;
    }

    public void setCode(String code) { this.code = code; }

    public String getName() {
        return name;
    }

    public void setName(String name) { this.name = name; }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) { this.description = description; }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) { this.keywords = keywords; }
}

