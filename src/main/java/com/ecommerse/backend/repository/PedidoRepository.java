package com.ecommerse.backend.repository;

import com.ecommerse.backend.model.Pedido;
import com.ecommerse.backend.model.enums.EstadoPedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByUsuario_Id(Long id);

    List<Pedido> findByEstadoPedido(EstadoPedido estadoPedido);
    List<Pedido> findByUsuario_IdAndEstadoPedido(Long id, EstadoPedido estadoPedido);

    /*Optional<Pedido> findBycarritoIdOriginal(Long idExternal);*/
}
