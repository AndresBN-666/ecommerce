package com.ecommerse.backend.dto.producto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductoDTO {

    private Long id;

    private String nombre;

    private String descripcion;

    private Double precio;

    private Integer stock;

    private String imagenUrl;

    private String nombreCategoria;
}
