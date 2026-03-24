package com.example.josalf.inventarios.service;

import com.example.josalf.inventarios.dto.*;
import com.example.josalf.inventarios.model.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class IngestionService {

    private final RestTemplate restTemplate;
    private final String URL_PROVIDER_A = "https://fakestoreapi.com/products";
    private final String URL_PROVIDER_B = "https://dummyjson.com/products";

    // Llamada Asíncrona al Proveedor A
    @Async // Spring ejecuta esto en otro hilo
    public CompletableFuture<List<HomologatedProduct>> fetchAndHomologateProviderA() {
        log.info("Iniciando llamado a Provider A");
        try {
            // Hacemos el GET al proveedor A
            ProviderAProduct[] response = restTemplate.getForObject(URL_PROVIDER_A, ProviderAProduct[].class);
            
            if (response == null) return CompletableFuture.completedFuture(Collections.emptyList());

            // Reglas de Negocio para Homologación A
            List<HomologatedProduct> homologated = List.of(response).stream()
                .map(p -> HomologatedProduct.builder()
                    .internalId("P1_" + p.getId()) // ID Único (ej: P1_101)
                    .title(p.getTitle())
                    .price(p.getPrice())
                    .normalizedRating(p.getRating() != null ? p.getRating().getRate() : 0.0) // Rating normalizado
                    .stock(0) // Regla de Negocio: Stock por defecto 0 para A
                    .originalProvider("ProviderA")
                    .syncedAt(LocalDateTime.now())
                    .build())
                .collect(Collectors.toList());
            
            log.info("Provider A sincronizado exitosamente ({} productos)", homologated.size());
            return CompletableFuture.completedFuture(homologated);

        } catch (RestClientException e) {
            // RESILIENCIA: Si falla, logueamos el error y devolvemos lista vacía para no romper el flujo
            log.error("Error llamando a Provider A. Se ignorará esta fuente. Detalle: {}", e.getMessage());
            return CompletableFuture.completedFuture(Collections.emptyList());
        }
    }

    // Llamada Asíncrona al Proveedor B
    @Async
    public CompletableFuture<List<HomologatedProduct>> fetchAndHomologateProviderB() {
        log.info("Iniciando llamado a Provider B");
        try {
            ProviderBResponse response = restTemplate.getForObject(URL_PROVIDER_B, ProviderBResponse.class);
            
            if (response == null || response.getProducts() == null) 
                return CompletableFuture.completedFuture(Collections.emptyList());

            // Reglas de Negocio para Homologación B
            List<HomologatedProduct> homologated = response.getProducts().stream()
                .map(p -> HomologatedProduct.builder()
                    .internalId("P2_" + p.getId()) // ID Único (ej: P2_101)
                    .title(p.getTitle())
                    .price(p.getPrice())
                    .normalizedRating(p.getRating()) // Rating directo
                    .stock(p.getStock()) // Sí incluye stock
                    .originalProvider("ProviderB")
                    .syncedAt(LocalDateTime.now())
                    .build())
                .collect(Collectors.toList());

            log.info("Provider B sincronizado exitosamente ({} productos)", homologated.size());
            return CompletableFuture.completedFuture(homologated);

        } catch (RestClientException e) {
            // RESILIENCIA: Igual que con A
            log.error("Error llamando a Provider B. Se ignorará esta fuente. Detalle: {}", e.getMessage());
            return CompletableFuture.completedFuture(Collections.emptyList());
        }
    }
}