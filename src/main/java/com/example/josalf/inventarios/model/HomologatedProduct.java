package com.example.josalf.inventarios.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_homologated")
@Data
@Builder 
@NoArgsConstructor
@AllArgsConstructor

public class HomologatedProduct {

    @Id // Esta será nuestra Llave Única para crear los id PX_
    private String internalId; 
    
    private String title;
    private double price;
    private double normalizedRating;
    private int stock; // (A=0 por defecto)
    private String originalProvider; // "ProviderA" o "ProviderB"
    
    //una fecha que nos ayude a saber cuando se sincronizó
    private LocalDateTime syncedAt; 
}
