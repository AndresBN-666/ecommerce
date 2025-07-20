package com.ecommerse.backend.repository;

import com.ecommerse.backend.model.ItemCarrito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemCarritoRepository extends JpaRepository<ItemCarrito, Long> {

    List<ItemCarrito> findByCarrito_Id(Long carritoId);
}
