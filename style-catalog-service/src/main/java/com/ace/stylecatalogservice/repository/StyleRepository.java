package com.ace.stylecatalogservice.repository;

import com.ace.stylecatalogservice.model.Style;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface StyleRepository extends MongoRepository<Style, String> {

    Optional<Style> findByCode(String code);
}