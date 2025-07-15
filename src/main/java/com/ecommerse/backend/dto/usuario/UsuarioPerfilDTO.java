package com.ecommerse.backend.dto.usuario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioPerfilDTO {
    private Long id;
    private String correo;
    private String rol;
}
