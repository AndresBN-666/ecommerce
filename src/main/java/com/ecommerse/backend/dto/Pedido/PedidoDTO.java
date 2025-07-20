package com.ecommerse.backend.dto.Pedido;

import com.ecommerse.backend.model.enums.EstadoPedido;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PedidoDTO {

    private Long pedidoId;
    private LocalDateTime fecha;
    private Double total;
    private List<DetallePedidoDTO> detalles;
}
