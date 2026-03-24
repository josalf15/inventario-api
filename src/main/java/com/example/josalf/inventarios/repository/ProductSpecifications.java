package com.example.josalf.inventarios.repository;
import com.example.josalf.inventarios.model.*;

import org.springframework.data.jpa.domain.Specification;

// clase para utiizar filtros, tmabien fue algo nuevo que tuve que investigar y tomar el codigo.
public class ProductSpecifications {

    public static Specification<HomologatedProduct> hasMinRating(Double minRating) {
        return (root, query, cb) -> 
            minRating == null ? null : cb.greaterThanOrEqualTo(root.get("normalizedRating"), minRating);
    }

    public static Specification<HomologatedProduct> hasMaxPrice(Double maxPrice) {
        return (root, query, cb) -> 
            maxPrice == null ? null : cb.lessThanOrEqualTo(root.get("price"), maxPrice);
    }

    public static Specification<HomologatedProduct> hasMinStock(Integer minStock) {
        return (root, query, cb) -> 
            minStock == null ? null : cb.greaterThanOrEqualTo(root.get("stock"), minStock);
    }

    public static Specification<HomologatedProduct> hasProvider(String provider) {
        return (root, query, cb) -> 
            provider == null ? null : cb.equal(root.get("originalProvider"), provider);
    }
}
