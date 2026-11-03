package com.example.cadastroclientes.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.cadastroclientes.model.Admin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class TokenService {

    @Value("${jwt.secret}")
    private String secret;

    public String gerarToken(Admin admin) {
        Algorithm algoritmo = Algorithm.HMAC256(secret);
        return JWT.create()
                .withIssuer("cadastro-clientes")
                .withSubject(admin.getEmail())
                .withExpiresAt(Instant.now().plus(8, ChronoUnit.HOURS))
                .sign(algoritmo);
    }

    public String validarToken(String token) {
        try {
            Algorithm algoritmo = Algorithm.HMAC256(secret);
            return JWT.require(algoritmo).withIssuer("cadastro-clientes").build()
                    .verify(token).getSubject();
        } catch (JWTVerificationException e) {
            return null;
        }
    }
}
