package com.ecommerse.backend.dto.carrito;

import lombok.Data;

@Data
public class ActualizarItemDTO {

    private Long productoId;
    private Integer cantidad;
}
