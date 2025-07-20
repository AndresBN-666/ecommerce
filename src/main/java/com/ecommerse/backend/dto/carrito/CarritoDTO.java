package com.ecommerse.backend.dto.carrito;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarritoDTO {
    private Long carritoId;
    private List<ItemCarritoDTO> items;
    private Double total;
}
