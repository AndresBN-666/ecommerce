package com.ecommerse.backend.dto.carrito;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AgregarItemDTO {
    private Long productoId;
    private Integer cantidad;
}
