package com.ecommerse.backend.controller;

import com.ecommerse.backend.dto.usuario.UsuarioPerfilDTO;
import com.ecommerse.backend.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Operation(summary = "Obtener perfil de usuario")
    @ApiResponse(responseCode = "200", description = "Perfil de usuario logueado")
    @PreAuthorize("hasAnyRole('CLIENTE', 'ADMIN')")
    @GetMapping("/perfil")
    public ResponseEntity<UsuarioPerfilDTO> obtenerPerfil(){
        return ResponseEntity.ok(usuarioService.obtenerPerfil());
    }
}
