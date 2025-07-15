package com.ecommerse.backend.service.impl;

import com.ecommerse.backend.dto.categoria.CategoriaDTO;
import com.ecommerse.backend.dto.categoria.CrearCategoriaDTO;
import com.ecommerse.backend.mapper.CategoriaMapper;
import com.ecommerse.backend.model.Categoria;
import com.ecommerse.backend.repository.CategoriaRepository;
import com.ecommerse.backend.service.CategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaServiceImpl implements CategoriaService {
    private final CategoriaRepository categoriaRepository;
    private final CategoriaMapper maper;

    @Override
    public CategoriaDTO crear(CrearCategoriaDTO dto) {
        Categoria categoria = maper.toEntity(dto);
        return maper.toDTO(categoriaRepository.save(categoria));
    }

    @Override
    public List<CategoriaDTO> listarTodas() {
        return maper.toListDTO(categoriaRepository.findAll());
    }

    @Override
    public void eliminar(Long id) {
        if(!categoriaRepository.existsById(id)){
            throw new RuntimeException("Producto no encontrado");
        }
        categoriaRepository.deleteById(id);
    }
}
