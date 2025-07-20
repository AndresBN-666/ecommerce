package com.ecommerse.backend.service;

import com.ecommerse.backend.dto.carrito.ActualizarItemDTO;
import com.ecommerse.backend.dto.carrito.AgregarItemDTO;
import com.ecommerse.backend.dto.carrito.CarritoDTO;

public interface CarritoService {

    CarritoDTO agregarProductoAlCarrito(AgregarItemDTO dto);
    CarritoDTO obtenerCarrito();
    CarritoDTO eliminarItemCarrito(Long id);
    CarritoDTO actualizarCantidad(ActualizarItemDTO dto);


}
