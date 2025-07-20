package com.ecommerse.backend.mapper;

import com.ecommerse.backend.dto.Pedido.DetallePedidoDTO;
import com.ecommerse.backend.dto.Pedido.PedidoDTO;
import com.ecommerse.backend.model.DetallePedido;
import com.ecommerse.backend.model.Pedido;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PedidoMapper {

    @Mapping(source = "id", target = "pedidoId")
    @Mapping(source = "detalles", target = "detalles")
    PedidoDTO toDTO(Pedido pedido);

    List<PedidoDTO> toDTOList(List<Pedido> pedidos);

    @Mapping(source = "producto.id", target = "productoId")
    @Mapping(source = "producto.nombre", target = "nombreProducto")
    @Mapping(source = "precioUnitario", target = "precioUnitario")
    @Mapping(source = "cantidad", target = "cantidad")
    @Mapping(source = "subtotal", target = "subtotal")
    DetallePedidoDTO toDetalleDTO(DetallePedido detalle);

    List<DetallePedidoDTO> toDetalleDTOList(List<DetallePedido> detalles);
}
