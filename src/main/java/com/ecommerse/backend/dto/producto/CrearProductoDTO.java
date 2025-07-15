package com.ecommerse.backend.dto.producto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CrearProductoDTO {

    @NotBlank(message = "Campo debe estar completo")
    private String nombre;

    private String descripcion;

    @NotNull
    @DecimalMin("0.0")
    private Double precio;

    @NotNull
    @Min(0)
    private Integer stock;

    private String imagenUrl;

    @NotNull
    private Long categoriaId;
}
