package com.ecommerse.backend.repository;

import com.ecommerse.backend.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByCategoria_Id(Long categoriaId);
}
