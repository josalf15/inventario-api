package com.example.josalf.inventarios.repository;

import com.example.josalf.inventarios.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;//algo nuevo a utiizar para mejores consultasen este las especificaicones
import org.springframework.stereotype.Repository;

@Repository
public interface HomologatedProductRepository extends JpaRepository<HomologatedProduct, String>, JpaSpecificationExecutor<HomologatedProduct> {
}
