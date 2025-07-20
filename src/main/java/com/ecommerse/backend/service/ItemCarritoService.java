package com.ecommerse.backend.service;

import com.ecommerse.backend.model.Carrito;
import com.ecommerse.backend.model.ItemCarrito;

public interface ItemCarritoService {

    ItemCarrito agregarItem(Carrito carrito, Long productoId, Integer cantidad);
    void eliminarItemDelCarrito(Carrito carrito, Long productoId);
    void actualizarCantidadDelProducto(Carrito carrito, Long productoId, Integer cantidad);
}
