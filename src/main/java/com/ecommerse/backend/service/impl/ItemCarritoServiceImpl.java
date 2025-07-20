package com.ecommerse.backend.service.impl;

import com.ecommerse.backend.model.Carrito;
import com.ecommerse.backend.model.ItemCarrito;
import com.ecommerse.backend.model.Producto;
import com.ecommerse.backend.repository.ItemCarritoRepository;
import com.ecommerse.backend.repository.ProductoRepository;
import com.ecommerse.backend.service.ItemCarritoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ItemCarritoServiceImpl implements ItemCarritoService {

    private final ProductoRepository productoRepository;
    private final ItemCarritoRepository itemCarritoRepository;

    @Override
    public ItemCarrito agregarItem(Carrito carrito, Long productoId, Integer cantidad) {

        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(()-> new RuntimeException("Producto no existe"));

        ItemCarrito itemCarrito = null;
        for (ItemCarrito item : carrito.getItems()){
            if (item.getProducto().getId().equals(productoId)){
                itemCarrito = item;
                break;
            }
        }

        if (itemCarrito!=null){
            itemCarrito.setCantidad(itemCarrito.getCantidad() + cantidad);
            itemCarrito.setSubtotal(producto.getPrecio() * itemCarrito.getCantidad());
            return itemCarritoRepository.save(itemCarrito);
        }else {
            ItemCarrito nuevoItem = ItemCarrito.builder()
                    .carrito(carrito)
                    .producto(producto)
                    .cantidad(cantidad)
                    .subtotal(producto.getPrecio() * cantidad)
                    .build();
            return itemCarritoRepository.save(nuevoItem);
        }

    }

    @Override
    public void eliminarItemDelCarrito(Carrito carrito, Long productoId) {
        ItemCarrito item = null;
        for (ItemCarrito itemCarrito : carrito.getItems()){
            if (itemCarrito.getProducto().getId().equals(productoId)){
                item = itemCarrito;
                break;
            }
        }
        if (item != null){
            itemCarritoRepository.delete(item);
            carrito.getItems().remove(item);
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado en el carrito");
        }
    }

    @Override
    public void actualizarCantidadDelProducto(Carrito carrito, Long productoId, Integer cantidad) {
        ItemCarrito itemCarrito = null;
        for (ItemCarrito item : carrito.getItems()){
            if (item.getProducto().getId().equals(productoId)){
                itemCarrito = item;
                break;
            }
        }

        if (itemCarrito != null){
            if (cantidad<=0){
                itemCarritoRepository.delete(itemCarrito);
                carrito.getItems().remove(itemCarrito);
            }else {
                itemCarrito.setCantidad(cantidad);
                itemCarrito.setSubtotal(cantidad * itemCarrito.getProducto().getPrecio());
                itemCarritoRepository.save(itemCarrito);
            }
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado en el carrito");
        }
    }
}