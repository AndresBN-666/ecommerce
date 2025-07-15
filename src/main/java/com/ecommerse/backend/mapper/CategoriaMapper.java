package com.ecommerse.backend.mapper;

import com.ecommerse.backend.dto.categoria.CategoriaDTO;
import com.ecommerse.backend.dto.categoria.CrearCategoriaDTO;
import com.ecommerse.backend.model.Categoria;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoriaMapper{

    CategoriaDTO toDTO(Categoria categoria);

    Categoria toEntity(CrearCategoriaDTO dto);

    List<CategoriaDTO> toListDTO(List<Categoria> categorias);

}
