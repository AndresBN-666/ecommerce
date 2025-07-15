package com.ecommerse.backend.controller;

import com.ecommerse.backend.dto.producto.CrearProductoDTO;
import com.ecommerse.backend.dto.producto.ProductoDTO;
import com.ecommerse.backend.service.CloudinaryService;
import com.ecommerse.backend.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/producto")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService service;
    private final CloudinaryService cloudinaryService;

    @Operation(summary = "Listar todos los Productos")
    @ApiResponse(responseCode = "200", description = "Listado Completo de todos los productos")
    @GetMapping("/listarTodos")
    public ResponseEntity<List<ProductoDTO>> listarTodos(){
        return ResponseEntity.ok(service.listarTodos());
    }

    @Operation(summary = "Buscar producto por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto encontrado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> obtenerPorId(@PathVariable Long id){
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @Operation(summary = "Crear nuevo producto")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Producto creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ProductoDTO> crear(@RequestBody CrearProductoDTO dto){
        System.out.println("DTO recibido: " + dto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.crear(dto));
    }


    @Operation(summary = "Actualizar un producto existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ProductoDTO> actualizar(@PathVariable Long id,
                                                  @RequestBody CrearProductoDTO productoDTO){
        return ResponseEntity.ok(service.actualizarProducto(id, productoDTO));
    }

    @Operation(summary = "Eliminar producto por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Categoria eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Categoria no encontrado")
    })

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void>eliminar(@PathVariable Long id){
        service.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }


    @Operation(
            summary = "Subir imagen de producto",
            description = "Sube una imagen a Cloudinary para un producto existente. Solo accesible por rol ADMIN."
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/{id}/imagen", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> subirImagen(
            @Parameter(description = "ID del producto") @PathVariable Long id,
            @Parameter(description = "Archivo de imagen", required = true)
            @RequestPart("archivo") MultipartFile archivo){
        String url = cloudinaryService.subirImagen(archivo);
        service.actualizarImagen(id, url);
        return ResponseEntity.ok(url);
    }
}

