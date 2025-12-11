package com.ace.stylecatalogservice.config;


import com.ace.stylecatalogservice.model.ColorPalette;
import com.ace.stylecatalogservice.model.Style;
import com.ace.stylecatalogservice.repository.ColorPaletteRepository;
import com.ace.stylecatalogservice.repository.StyleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final StyleRepository styleRepository;
    private final ColorPaletteRepository colorPaletteRepository;

    public DataInitializer(StyleRepository styleRepository,
                           ColorPaletteRepository colorPaletteRepository) {
        this.styleRepository = styleRepository;
        this.colorPaletteRepository = colorPaletteRepository;
    }

    @Override
    public void run(String... args) {
        if (styleRepository.count() == 0) {
            Style scandi = new Style(
                    "SCANDI",
                    "Style Scandinave",
                    "Design lumineux, épuré, avec bois clair, blanc et plantes vertes.",
                    Arrays.asList("wood", "white walls", "plants", "minimalist", "natural light")
            );

            Style modern = new Style(
                    "MODERN",
                    "Style Moderne",
                    "Lignes épurées, couleurs neutres, touches de noir et métal.",
                    Arrays.asList("modern", "clean lines", "neutral colors", "metal", "black accents")
            );

            Style classic = new Style(
                    "CLASSIC",
                    "Style Classique",
                    "Moulures, meubles élégants, tons chauds et symétrie.",
                    Arrays.asList("elegant", "warm colors", "moldings", "symmetry", "traditional furniture")
            );

            Style gaming = new Style(
                    "GAMING",
                    "Style Gaming",
                    "Ambiance immersive avec LED, couleurs sombres et setup gamer.",
                    Arrays.asList("RGB lights", "dark walls", "gaming desk", "LED strips", "futuristic")
            );

            styleRepository.saveAll(List.of(scandi, modern, classic, gaming));
        }

        if (colorPaletteRepository.count() == 0) {
            ColorPalette nordic = new ColorPalette(
                    "Nordic Light",
                    "Blanc, bois clair et touches de vert doux.",
                    Arrays.asList("#FFFFFF", "#E5E7EB", "#D1FAE5", "#F3F4F6", "#A7F3D0")
            );

            ColorPalette modernDark = new ColorPalette(
                    "Modern Dark",
                    "Contraste fort avec gris foncé, noir et accents métalliques.",
                    Arrays.asList("#111827", "#1F2937", "#374151", "#9CA3AF", "#F9FAFB")
            );

            ColorPalette warmClassic = new ColorPalette(
                    "Warm Classic",
                    "Beige, or, bois foncé, ambiance chaleureuse.",
                    Arrays.asList("#F5F5DC", "#B45309", "#92400E", "#78350F", "#FAF5E4")
            );

            ColorPalette neonGaming = new ColorPalette(
                    "Neon Gaming",
                    "Fond sombre et accents néon violet/bleu.",
                    Arrays.asList("#020617", "#0F172A", "#6366F1", "#A855F7", "#22D3EE")
            );

            colorPaletteRepository.saveAll(List.of(nordic, modernDark, warmClassic, neonGaming));
        }
    }
}
