package com.ecommerse.backend.auth;

import com.ecommerse.backend.dto.registro.LoginDTO;
import com.ecommerse.backend.dto.registro.RegistrarDTO;
import com.ecommerse.backend.jwt.JwtService;
import com.ecommerse.backend.model.Usuario;
import com.ecommerse.backend.model.enums.Rol;
import com.ecommerse.backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;


    public AuthResponse registrar(RegistrarDTO dto){
        if(usuarioRepository.existsByCorreo(dto.getCorreo())){
            throw new RuntimeException("El correo ya se encuentra registrado");
        }
        Usuario usuario = Usuario.builder()
                .nombre(dto.getNombre())
                .correo(dto.getCorreo())
                .clave(passwordEncoder.encode(dto.getClave()))
                .rol(dto.getRol() != null ? dto.getRol() : Rol.CLIENTE)
                .build();
        usuarioRepository.save(usuario);
        return new AuthResponse("Usuario registrado correctamente");
    }

    public AuthToken login(LoginDTO dto){

        Usuario usuario = usuarioRepository.findByCorreo(dto.getCorreo())
                .orElseThrow(() -> new RuntimeException("Correo no registrado"));
        if (!passwordEncoder.matches(dto.getClave(), usuario.getClave())){
            throw  new RuntimeException("contraseña incorrecta");
        }
        String token = jwtService.generarToken(usuario);
        return new AuthToken(token);
    }
}
