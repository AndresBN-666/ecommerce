package com.ecommerse.backend.service;

import com.ecommerse.backend.dto.categoria.CategoriaDTO;
import com.ecommerse.backend.dto.categoria.CrearCategoriaDTO;

import java.util.List;

public interface CategoriaService {

    CategoriaDTO crear(CrearCategoriaDTO dto);
    List<CategoriaDTO> listarTodas();
    void eliminar(Long id);
}
