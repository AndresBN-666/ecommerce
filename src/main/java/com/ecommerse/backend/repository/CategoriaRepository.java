package com.ecommerse.backend.repository;

import com.ecommerse.backend.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}
