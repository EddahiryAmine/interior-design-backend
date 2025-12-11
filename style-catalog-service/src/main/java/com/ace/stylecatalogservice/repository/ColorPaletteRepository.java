package com.ace.stylecatalogservice.repository;

import com.ace.stylecatalogservice.model.ColorPalette;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ColorPaletteRepository extends MongoRepository<ColorPalette, String> {
}
