package com.ecommerse.backend.service;

import com.ecommerse.backend.dto.producto.CrearProductoDTO;
import com.ecommerse.backend.dto.producto.ProductoDTO;

import java.util.List;

public interface ProductoService {
    List<ProductoDTO> listarTodos();
    ProductoDTO crear(CrearProductoDTO dto);
    ProductoDTO obtenerPorId(Long id);
    ProductoDTO actualizarProducto(Long id, CrearProductoDTO dto);
    void eliminarProducto(Long id);
    void actualizarImagen(Long idProducto, String urlImagen);
}
