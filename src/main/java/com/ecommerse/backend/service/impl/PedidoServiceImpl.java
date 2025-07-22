package com.ecommerse.backend.service.impl;

import com.ecommerse.backend.dto.Pedido.PedidoDTO;
import com.ecommerse.backend.mapper.PedidoMapper;
import com.ecommerse.backend.model.*;
import com.ecommerse.backend.model.enums.EstadoPedido;
import com.ecommerse.backend.repository.CarritoRepository;
import com.ecommerse.backend.repository.PedidoRepository;
import com.ecommerse.backend.repository.ProductoRepository;
import com.ecommerse.backend.repository.UsuarioRepository;
import com.ecommerse.backend.service.DetallePedidoService;
import com.ecommerse.backend.service.PedidoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final PedidoMapper pedidoMapper;
    private final UsuarioRepository usuarioRepository;
    private final CarritoRepository carritoRepository;
    private final ProductoRepository productoRepository;

    private String correoAutenticado() {
        return SecurityContextHolder.getContext()
                .getAuthentication().getName();
    }


    @Override
    @Transactional
    public PedidoDTO finalizarCompra() {

        String correoAutenticado = correoAutenticado();

        Usuario usuario = usuarioRepository.findByCorreo(correoAutenticado)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Usuario no registrado"));

        Carrito carrito = carritoRepository.findByUsuario_IdAndActivoTrue(usuario.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Cliente no tiene un carrito activo"));
        
        if (carrito.getItems().isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "El carrito esta vacio");
        }

        carrito.setActivo(false);
        carritoRepository.save(carrito);

        List<Producto> productosActualizados = new ArrayList<>();

        for (ItemCarrito itemStock : carrito.getItems()){
            Producto producto = itemStock.getProducto();
            if (producto.getStock() < itemStock.getCantidad()){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Stock insuficiente para el producto: " + producto.getNombre());
            }
            producto.setStock(producto.getStock()-itemStock.getCantidad());
            productosActualizados.add(producto);
        }

        productoRepository.saveAll(productosActualizados);



        Pedido pedido = Pedido.builder()
                .usuario(usuario)
                .fecha(LocalDateTime.now())
                .total(carrito.getItems().stream()
                        .mapToDouble(ItemCarrito::getSubtotal)
                        .sum())
                .estadoPedido(EstadoPedido.PENDIENTE)
                .build();

        for (ItemCarrito itemCarrito : carrito.getItems()){
            DetallePedido detallePedido = DetallePedido.builder()
                    .producto(itemCarrito.getProducto())
                    .cantidad(itemCarrito.getCantidad())
                    .precioUnitario(itemCarrito.getProducto().getPrecio())
                    .subtotal(itemCarrito.getSubtotal())
                    .pedido(pedido)
                    .build();
            pedido.getDetalles().add(detallePedido);
        }

        pedidoRepository.save(pedido);

        return pedidoMapper.toDTO(pedido);
    }

    @Override
    public List<PedidoDTO> listarPedidos() {
        String correoAutenticado = correoAutenticado();

        Usuario usuario = usuarioRepository.findByCorreo(correoAutenticado)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Usuario no encontrado"));

        List<Pedido> pedidos = pedidoRepository.findByUsuario_Id(usuario.getId());

        return pedidoMapper.toDTOList(pedidos);
    }

    @Override
    public PedidoDTO obtenerPedidoPorId(Long id) {
        String correoAutenticado = correoAutenticado();

        Usuario usuario = usuarioRepository.findByCorreo(correoAutenticado)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Usuario no encontrado"));

        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Pedido no encontrado"));

        if (!pedido.getUsuario().getId().equals(usuario.getId())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tiene acceso a este pedido");
        }
        return pedidoMapper.toDTO(pedido);
    }

    @Override
    public PedidoDTO adminObtenerPedidoPorId(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Pedido no encontrado"));

        return pedidoMapper.toDTO(pedido);
    }

    @Override
    public List<PedidoDTO> listarTodosLosPedidosRolAdmin() {
        List<Pedido> pedidos = pedidoRepository.findAll();
        return pedidoMapper.toDTOList(pedidos);
    }

    @Override
    public PedidoDTO actualizarEstado(Long idPedido, EstadoPedido estado) {

        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Pedido no encontrado"));

        if (pedido.getEstadoPedido() == EstadoPedido.ENTREGADO){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se puede modificar un pedido ya entregado");
        }

        pedido.setEstadoPedido(estado);
        pedidoRepository.save(pedido);
        return pedidoMapper.toDTO(pedido);
    }

    @Override
    public List<PedidoDTO> buscarPorEstado(EstadoPedido estadoPedido) {

        List<Pedido> pedidos = pedidoRepository.findByEstadoPedido(estadoPedido);

        return pedidoMapper.toDTOList(pedidos);
    }

    @Override
    public EstadoPedido[] listarEstados() {
         EstadoPedido[] estadoPedidos = EstadoPedido.values();
         return estadoPedidos;
    }

    @Override
    public List<PedidoDTO> buscarMisPedidosPorEstado(EstadoPedido estado) {
        String correoAutenticado = correoAutenticado();
        Usuario usuario = usuarioRepository.findByCorreo(correoAutenticado)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Usuario no encontrado"));

        List<Pedido> pedido = pedidoRepository.findByUsuario_IdAndEstadoPedido(usuario.getId(),
                estado);

        return pedidoMapper.toDTOList(pedido);
    }

    @Override
    @Transactional
    public PedidoDTO finalizarCompraDesdeWebhook(Long carritoId) {

        Carrito carrito = carritoRepository.findById(carritoId) // Buscar por ID del carrito
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Carrito no encontrado para ID: " + carritoId));

        // Obtener el usuario directamente del carrito
        Usuario usuario = carrito.getUsuario();
        if (usuario == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Usuario asociado al carrito no encontrado.");
        }

        // Asegurarse de que no se procese dos veces si el webhook llega duplicado
        if (!carrito.isActivo()) {
            System.out.println("Carrito " + carritoId + " ya no está activo. Posible webhook duplicado.");
            // Opcional: puedes lanzar una excepción o simplemente retornar si ya está procesado
            // return pedidoMapper.toDTO(pedidoRepository.findByCarritoId(carritoId).orElse(null)); // Si ya guardas el pedido con ref de carrito
             throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                     "Carrito " + carritoId + " ya no está activo. Posible webhook duplicado."); // O una respuesta adecuada
        }

        if (carrito.getItems().isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "El carrito esta vacio");
        }

        carrito.setActivo(false);
        carritoRepository.save(carrito);

        List<Producto> productosActualizados = new ArrayList<>();

        for (ItemCarrito itemStock : carrito.getItems()){
            Producto producto = itemStock.getProducto();
            if (producto.getStock() < itemStock.getCantidad()){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Stock insuficiente para el producto: " + producto.getNombre());
            }
            producto.setStock(producto.getStock()-itemStock.getCantidad());
            productosActualizados.add(producto);
        }

        productoRepository.saveAll(productosActualizados);

        Pedido pedido = Pedido.builder()
                .usuario(usuario)
                .fecha(LocalDateTime.now())
                .total(carrito.getItems().stream()
                        .mapToDouble(ItemCarrito::getSubtotal)
                        .sum())
                .estadoPedido(EstadoPedido.PENDIENTE)
                .build();

        for (ItemCarrito itemCarrito : carrito.getItems()){
            DetallePedido detallePedido = DetallePedido.builder()
                    .producto(itemCarrito.getProducto())
                    .cantidad(itemCarrito.getCantidad())
                    .precioUnitario(itemCarrito.getProducto().getPrecio())
                    .subtotal(itemCarrito.getSubtotal())
                    .pedido(pedido)
                    .build();
            pedido.getDetalles().add(detallePedido);
        }

        pedidoRepository.save(pedido);

        return pedidoMapper.toDTO(pedido);

    }

/*    @Override
    public PedidoDTO getPedidoPorExternalReference(Long externalReference) {
        Pedido pedido = pedidoRepository.findBycarritoIdOriginal(externalReference)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido no encontrado para la referencia: " + externalReference));
        return pedidoMapper.toDTO(pedido);

    }*/
}
