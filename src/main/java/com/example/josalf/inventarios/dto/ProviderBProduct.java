package com.example.josalf.inventarios.dto;
import lombok.Data;

@Data
public class ProviderBProduct {
    private int id;
    private String title;
    private double price;
    private double rating; // Float directo
    private int stock; // Sí incluye stock
}