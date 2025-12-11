package com.ace.designservice.client;


import com.ace.designservice.dto.ColorPaletteSummary;
import com.ace.designservice.dto.StyleSummary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class StyleCatalogClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public StyleCatalogClient(RestTemplate restTemplate,
                              @Value("${style.catalog.service.base-url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public StyleSummary getStyleById(String styleId) {
        String url = baseUrl + "/styles/" + styleId;
        return restTemplate.getForObject(url, StyleSummary.class);
    }

    public ColorPaletteSummary getPaletteById(String paletteId) {
        String url = baseUrl + "/palettes/" + paletteId;
        return restTemplate.getForObject(url, ColorPaletteSummary.class);
    }
}
