package com.ecommerse.backend.service.impl;

import com.ecommerse.backend.model.Carrito;
import com.ecommerse.backend.model.DetallePedido;
import com.ecommerse.backend.model.ItemCarrito;
import com.ecommerse.backend.model.Pedido;
import com.ecommerse.backend.repository.DetallePedidoRepository;
import com.ecommerse.backend.service.DetallePedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DetallePedidoServiceImpl implements DetallePedidoService {

    private final DetallePedidoRepository detallePedidoRepository;


    @Override
    public void AgregarDetallePedido(Carrito carrito, Pedido pedido) {

    }
}
