package com.ecommerse.backend.repository;

import com.ecommerse.backend.model.Carrito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarritoRepository extends JpaRepository<Carrito, Long> {
    Optional<Carrito> findByUsuario_IdAndActivoTrue(Long usuarioId);
}
