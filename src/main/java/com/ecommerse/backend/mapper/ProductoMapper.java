package com.ecommerse.backend.mapper;

import com.ecommerse.backend.dto.producto.CrearProductoDTO;
import com.ecommerse.backend.dto.producto.ProductoDTO;
import com.ecommerse.backend.model.Producto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductoMapper {

    @Mapping(source = "categoria.nombre", target = "nombreCategoria")
    ProductoDTO toDTO(Producto producto);

    @Mapping(source = "categoriaId", target = "categoria.id")
    Producto toEntity(CrearProductoDTO dto);

    List<ProductoDTO> toDtoList(List<Producto> productos);

    @Mapping(target = "id", ignore = true)
    void actualizarDesdeDTO(CrearProductoDTO dto, @MappingTarget Producto producto);
}
