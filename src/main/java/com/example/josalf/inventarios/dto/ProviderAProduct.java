package com.example.josalf.inventarios.dto;
import lombok.Data;

@Data
public class ProviderAProduct {
    private int id; // Usaremos esto para crear el ID Único
    private String title;
    private double price;
    private String description;
    private String category;
    private String image;
    private Rating rating; // Objeto complejo 
    
    @Data
    public static class Rating {
        private double rate; // Este es el que nos interesa obtener
        private int count;
    }
}
