package com.ace.stylecatalogservice.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "color_palettes")
public class ColorPalette {

    @Id
    private String id;

    private String name;
    private String description;

    private List<String> colors;

    public ColorPalette() {
    }

    public ColorPalette(String name, String description, List<String> colors) {
        this.name = name;
        this.description = description;
        this.colors = colors;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) { this.id = id; }

    public String getName() {
        return name;
    }

    public void setName(String name) { this.name = name; }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) { this.description = description; }

    public List<String> getColors() {
        return colors;
    }

    public void setColors(List<String> colors) { this.colors = colors; }
}
