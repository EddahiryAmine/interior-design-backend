package com.ace.stylecatalogservice.controller;


import com.ace.stylecatalogservice.model.Style;
import com.ace.stylecatalogservice.repository.StyleRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/styles")
public class StyleController {

    private final StyleRepository styleRepository;

    public StyleController(StyleRepository styleRepository) {
        this.styleRepository = styleRepository;
    }

    @GetMapping
    public ResponseEntity<List<Style>> getAll() {
        return ResponseEntity.ok(styleRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Style> getById(@PathVariable String id) {
        return styleRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<Style> getByCode(@PathVariable String code) {
        return styleRepository.findByCode(code.toUpperCase())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Style> create(@Valid @RequestBody Style style) {
        if (style.getCode() != null) {
            style.setCode(style.getCode().toUpperCase());
        }
        Style saved = styleRepository.save(style);
        return ResponseEntity.ok(saved);
    }
}
