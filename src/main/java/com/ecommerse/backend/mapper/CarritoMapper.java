package com.ecommerse.backend.mapper;

import com.ecommerse.backend.dto.carrito.CarritoDTO;
import com.ecommerse.backend.dto.carrito.ItemCarritoDTO;
import com.ecommerse.backend.model.Carrito;
import com.ecommerse.backend.model.ItemCarrito;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CarritoMapper {


    @Mapping(source = "id", target = "carritoId")
    @Mapping(source = "items", target = "items")
    @Mapping(target = "total", expression = "java(calcularTotal(carrito))")
    CarritoDTO toDTO (Carrito carrito);

    @Mapping(source = "producto.id", target = "productoId")
    @Mapping(source = "producto.nombre", target = "nombreProducto")
    @Mapping(source = "producto.precio", target = "precioUnitario")
    ItemCarritoDTO toItemDTO(ItemCarrito itemCarrito);

    List<ItemCarritoDTO> DTO_LIST(List<ItemCarrito> items);


    //Metodo default para calcular el total
    default Double calcularTotal(Carrito carrito) {
        if (carrito == null || carrito.getItems() == null) {
            return 0.0;
        }

        return carrito.getItems()
                .stream()
                .mapToDouble(ItemCarrito::getSubtotal)
                .sum();
    }
}
