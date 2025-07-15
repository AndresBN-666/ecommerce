package com.ecommerse.backend.dto.categoria;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CrearCategoriaDTO {
    @NotBlank(message = "Campo no puede estar vacio")
    private String nombre;
}
