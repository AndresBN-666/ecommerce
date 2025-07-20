package com.ecommerse.backend.service;

import com.ecommerse.backend.model.Carrito;
import com.ecommerse.backend.model.Pedido;

public interface DetallePedidoService {
    void AgregarDetallePedido(Carrito carrito, Pedido pedido);
}
