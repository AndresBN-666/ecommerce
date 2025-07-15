package com.ecommerse.backend.service.impl;

import com.ecommerse.backend.dto.usuario.UsuarioPerfilDTO;
import com.ecommerse.backend.model.Usuario;
import com.ecommerse.backend.repository.UsuarioRepository;
import com.ecommerse.backend.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UsuarioPerfilDTO obtenerPerfil() {
        String correo = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() ->new RuntimeException("No existe usuario con correo: " + correo));
        return UsuarioPerfilDTO.builder()
                .id(usuario.getId())
                .correo(usuario.getCorreo())
                .rol(usuario.getRol().name())
                .build();
    }
}
