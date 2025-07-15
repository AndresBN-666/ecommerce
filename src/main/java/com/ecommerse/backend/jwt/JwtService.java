package com.ecommerse.backend.jwt;

import com.ecommerse.backend.model.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    private static final Key CLAVE_SECRETA= Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final long EXPIRATION_TIME = 1000*60*60*24;



    public String generarToken(Usuario usuario){
        return Jwts.builder()
                .setSubject(usuario.getCorreo())
                .claim("rol", usuario.getRol().name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(CLAVE_SECRETA)
                .compact();

    }

    public String extraerCorreoUsuario(String token){
        return Jwts.parserBuilder()
                .setSigningKey(CLAVE_SECRETA)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean esTokenValido(String token, Usuario usuario) {
        String correo = extraerCorreoUsuario(token);
        return correo.equals(usuario.getCorreo()) && !estaExpirado(token);
    }


    public boolean estaExpirado(String token) {
        Date expiration = Jwts.parserBuilder()
                .setSigningKey(CLAVE_SECRETA)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expiration.before(new Date());
    }

}
