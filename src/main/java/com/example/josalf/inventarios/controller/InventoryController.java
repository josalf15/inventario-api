package com.example.josalf.inventarios.controller;

import com.example.josalf.inventarios.model.*;
import com.example.josalf.inventarios.repository.HomologatedProductRepository;
import com.example.josalf.inventarios.repository.ProductSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventory") // URL que se definió en el req
@RequiredArgsConstructor
public class InventoryController {

    private final HomologatedProductRepository repository;

    @GetMapping
    public ResponseEntity<List<HomologatedProduct>> queryLocalInventory(
            // Todos los RequestParams son opcionales (required = false), esto fue algo nuevo que implmentar como parametros
            @RequestParam(required = false) Double minRating,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Integer minStock,
            @RequestParam(required = false) String provider
    ) {
        // Construimos la consulta
        Specification<HomologatedProduct> spec = Specification.<HomologatedProduct>unrestricted()
                .and(ProductSpecifications.hasMinRating(minRating))
                .and(ProductSpecifications.hasMaxPrice(maxPrice))
                .and(ProductSpecifications.hasMinStock(minStock))
                .and(ProductSpecifications.hasProvider(provider));

        // Ejecutamos la consulta en nuestra DB local
        List<HomologatedProduct> results = repository.findAll(spec);

        return ResponseEntity.ok(results);
    }
}