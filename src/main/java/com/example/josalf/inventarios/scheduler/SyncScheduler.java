package com.example.josalf.inventarios.scheduler;

import com.example.josalf.inventarios.model.*;
import com.example.josalf.inventarios.repository.HomologatedProductRepository;
import com.example.josalf.inventarios.service.IngestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Component
@Slf4j
@RequiredArgsConstructor
public class SyncScheduler {

    private final IngestionService ingestionService;
    private final HomologatedProductRepository repository;

    // Cron Job: Ejecutar cada 10 minutos
    // Sintaxis: segundos minutos horas día_mes mes día_semana, esto fue algo nuevo a utilizar tuve que investigar como se hace y tomar codigo 
    @Scheduled(cron = "0 */1 * * * *")//lo dejo en 1 minuto para validaciones
    public void executeSyncProcess() {
        log.info("--- INICIANDO PROCESO DE SINCRONIZACIÓN PROGRAMADO ---");

        // Lanzamos ambos procesos en paralelo
        CompletableFuture<List<HomologatedProduct>> futureA = ingestionService.fetchAndHomologateProviderA();
        CompletableFuture<List<HomologatedProduct>> futureB = ingestionService.fetchAndHomologateProviderB();

        // RESILIENCIA TOTAL: Esperamos a que ambos terminen (independientemente de si trajeron datos o fallaron)
        CompletableFuture.allOf(futureA, futureB).join();

        try {
            // Recuperamos los resultados
            List<HomologatedProduct> productsA = futureA.get();
            List<HomologatedProduct> productsB = futureB.get();

            List<HomologatedProduct> allProducts = new ArrayList<>();
            allProducts.addAll(productsA);
            allProducts.addAll(productsB);

            if (!allProducts.isEmpty()) {
                // Guardado masivo 
                repository.saveAll(allProducts);
                log.info("Se han actualizado/insertado exitosamente {} productos en la base de datos local.", allProducts.size());
            } else {
                log.warn("No se recuperaron productos de ninguna fuente externa en esta corrida.");
            }

        } catch (InterruptedException | ExecutionException e) {
            log.error("Error crítico durante la orquestación de sincronización: {}", e.getMessage());
        
        }

        log.info("--- FIN DEL PROCESO DE SINCRONIZACIÓN ---");
    }
}
