package com.ecommerse.backend.auth;

import com.ecommerse.backend.dto.registro.LoginDTO;
import com.ecommerse.backend.dto.registro.RegistrarDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    @PostMapping("/registrar")
    public ResponseEntity<AuthResponse> registro (@RequestBody @Valid RegistrarDTO dto){
        return ResponseEntity.ok(service.registrar(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthToken> login(@RequestBody LoginDTO dto){
        return ResponseEntity.ok(service.login(dto));
    }


}
