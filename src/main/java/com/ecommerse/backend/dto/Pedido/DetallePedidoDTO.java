package com.ecommerse.backend.dto.Pedido;

import lombok.Data;

@Data
public class DetallePedidoDTO {

    private Long productoId;
    private String nombreProducto;
    private Double precioUnitario;
    private Integer cantidad;
    private Double subtotal;
}
