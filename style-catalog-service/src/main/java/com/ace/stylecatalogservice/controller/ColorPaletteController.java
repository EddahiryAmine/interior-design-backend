package com.ace.stylecatalogservice.controller;


import com.ace.stylecatalogservice.model.ColorPalette;
import com.ace.stylecatalogservice.repository.ColorPaletteRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/palettes")
public class ColorPaletteController {

    private final ColorPaletteRepository colorPaletteRepository;

    public ColorPaletteController(ColorPaletteRepository colorPaletteRepository) {
        this.colorPaletteRepository = colorPaletteRepository;
    }

    @GetMapping
    public ResponseEntity<List<ColorPalette>> getAll() {
        return ResponseEntity.ok(colorPaletteRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ColorPalette> getById(@PathVariable String id) {
        return colorPaletteRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ColorPalette> create(@Valid @RequestBody ColorPalette palette) {
        ColorPalette saved = colorPaletteRepository.save(palette);
        return ResponseEntity.ok(saved);
    }
}

