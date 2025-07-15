package com.ecommerse.backend.controller;

import com.ecommerse.backend.dto.categoria.CategoriaDTO;
import com.ecommerse.backend.dto.categoria.CrearCategoriaDTO;
import com.ecommerse.backend.service.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categoria")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService service;

    @Operation(summary = "Listar Todas las Categorias")
    @ApiResponse(responseCode = "200", description = "Listado Completo de todas las Categorias")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<CategoriaDTO>> listar(){
        return ResponseEntity.ok(service.listarTodas());
    }

    @Operation(summary = "Crear nueva categoria")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Categoria creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CategoriaDTO> crear(@RequestBody @Valid CrearCategoriaDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.crear(dto));
    }

    @Operation(summary = "Eliminar categoria por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Categoria eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Categoria no encontrado")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id){
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
