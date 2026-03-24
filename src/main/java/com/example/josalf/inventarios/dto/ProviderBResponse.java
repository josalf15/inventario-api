package com.example.josalf.inventarios.dto;
import lombok.Data;
import java.util.List;

@Data
public class ProviderBResponse {
    // APi provedor B devuelve products lo que hace manejarlo en una lista
    private List<ProviderBProduct> products; 
}
