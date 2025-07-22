package com.ecommerse.backend.service;

import com.ecommerse.backend.dto.Pedido.PedidoDTO;
import com.ecommerse.backend.model.enums.EstadoPedido;

import java.util.List;

public interface PedidoService {

    PedidoDTO finalizarCompra();

    List<PedidoDTO> listarPedidos();

    PedidoDTO obtenerPedidoPorId(Long id);

    PedidoDTO adminObtenerPedidoPorId(Long id);

    List<PedidoDTO> listarTodosLosPedidosRolAdmin();

    PedidoDTO actualizarEstado(Long idPedido, EstadoPedido estado);

    List<PedidoDTO> buscarPorEstado(EstadoPedido estadoPedido);

    EstadoPedido[] listarEstados();

    List<PedidoDTO> buscarMisPedidosPorEstado(EstadoPedido estado);

    // finalizar compra desde Webhook
    PedidoDTO finalizarCompraDesdeWebhook(Long carritoId);

/*    PedidoDTO getPedidoPorExternalReference(Long externalReference);*/

}
