package com.ecommerse.backend.service.impl;

import com.ecommerse.backend.dto.carrito.ActualizarItemDTO;
import com.ecommerse.backend.dto.carrito.AgregarItemDTO;
import com.ecommerse.backend.dto.carrito.CarritoDTO;
import com.ecommerse.backend.mapper.CarritoMapper;
import com.ecommerse.backend.model.Carrito;
import com.ecommerse.backend.model.Producto;
import com.ecommerse.backend.model.Usuario;
import com.ecommerse.backend.repository.CarritoRepository;
import com.ecommerse.backend.repository.ProductoRepository;
import com.ecommerse.backend.repository.UsuarioRepository;
import com.ecommerse.backend.service.CarritoService;
import com.ecommerse.backend.service.ItemCarritoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CarritoServiceImpl implements CarritoService {

    private final CarritoRepository carritoRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ItemCarritoService itemCarritoService;
    private final CarritoMapper carritoMapper;

    private String correoAutenticado() {
        return SecurityContextHolder.getContext()
                .getAuthentication().getName();
    }

    @Override
    @Transactional
    public CarritoDTO agregarProductoAlCarrito(AgregarItemDTO dto) {

        String correoUsuario = correoAutenticado();

        Usuario usuario = usuarioRepository.findByCorreo(correoUsuario)
                .orElseThrow(()-> new RuntimeException("Para continuar debe registrarse"));

        Producto producto = productoRepository.findById(dto.getProductoId())
                .orElseThrow(()-> new RuntimeException("Producto no encontrado"));

        if (producto.getStock() < dto.getCantidad()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Stock insuficiente");
        }


        Carrito carrito = carritoRepository.findByUsuario_IdAndActivoTrue(usuario.getId())
                .orElseGet(() ->{
                    Carrito nuevoCarrito = Carrito.builder()
                            .usuario(usuario)
                            .activo(true)
                            .items(new ArrayList<>())
                            .build();
                    return carritoRepository.save(nuevoCarrito);
                });

        itemCarritoService.agregarItem(carrito,dto.getProductoId(), dto.getCantidad());

        // 🔁 Recargar carrito actualizado desde base de datos
        Carrito carritoActualizado = carritoRepository.findById(carrito.getId())
                .orElseThrow(() -> new RuntimeException("Error al obtener carrito actualizado"));

        return carritoMapper.toDTO(carritoActualizado);
    }

    @Override
    public CarritoDTO obtenerCarrito() {
        String correoUsuario = correoAutenticado();

        Usuario usuario = usuarioRepository.findByCorreo(correoUsuario).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no registrado"));

        Carrito carrito = carritoRepository.findByUsuario_IdAndActivoTrue(usuario.getId()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "El cliente no tiene un carrito activo"));

        return carritoMapper.toDTO(carrito);
    }

    @Override
    @Transactional
    public CarritoDTO eliminarItemCarrito(Long id) {

        String correoUsuario = correoAutenticado();

        Usuario usuario = usuarioRepository.findByCorreo(correoUsuario).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no registrado"));

        Carrito carrito = carritoRepository.findByUsuario_IdAndActivoTrue(usuario.getId()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no tiene un carrito activo"));

        itemCarritoService.eliminarItemDelCarrito(carrito, id);

        return carritoMapper.toDTO(carrito);
    }

    @Override
    public CarritoDTO actualizarCantidad(ActualizarItemDTO dto) {
        String correoUsuario = correoAutenticado();

        Usuario usuario = usuarioRepository.findByCorreo(correoUsuario).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no registrado"));

        Carrito carrito = carritoRepository.findByUsuario_IdAndActivoTrue(usuario.getId()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no tiene un carrito activo"));

        itemCarritoService.actualizarCantidadDelProducto(carrito, dto.getProductoId(), dto.getCantidad());

        return carritoMapper.toDTO(carrito);
    }
}
