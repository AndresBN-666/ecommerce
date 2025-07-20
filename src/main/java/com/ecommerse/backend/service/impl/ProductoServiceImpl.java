package com.ecommerse.backend.service.impl;

import com.ecommerse.backend.dto.producto.CrearProductoDTO;
import com.ecommerse.backend.dto.producto.ProductoDTO;
import com.ecommerse.backend.mapper.ProductoMapper;
import com.ecommerse.backend.model.Categoria;
import com.ecommerse.backend.model.Producto;
import com.ecommerse.backend.repository.CategoriaRepository;
import com.ecommerse.backend.repository.ProductoRepository;
import com.ecommerse.backend.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final ProductoMapper mapper;


    @Override
    public List<ProductoDTO> listarTodos() {
        return mapper.toDtoList(productoRepository.findAll());
    }

    @Override
    public ProductoDTO crear(CrearProductoDTO dto) {
        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(()-> new RuntimeException("Categoria no encontrada"));

        Producto producto = mapper.toEntity(dto);
        producto.setCategoria(categoria);
        return mapper.toDTO(productoRepository.save(producto));
    }

    @Override
    public ProductoDTO obtenerPorId(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        return mapper.toDTO(producto);
    }

    @Override
    public ProductoDTO actualizarProducto(Long id, CrearProductoDTO dto) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        mapper.actualizarDesdeDTO(dto,producto);

        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(()-> new RuntimeException("Categoria no encontrado"));

        producto.setCategoria(categoria);
        return mapper.toDTO(productoRepository.save(producto));
    }

    @Override
    public void eliminarProducto(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new RuntimeException("Producto no encontrado");
        }
        productoRepository.deleteById(id);

    }

    @Override
    public void actualizarImagen(Long idProducto, String urlImagen) {
        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        producto.setImagenUrl(urlImagen);
        productoRepository.save(producto);
    }

    @Override
    public List<ProductoDTO> buscarPorCategoria(Long idCategoria) {
        List<Producto> producto = productoRepository.findByCategoria_Id(idCategoria);
        return mapper.toDtoList(producto);
    }
}
