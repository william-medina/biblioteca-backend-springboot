package com.williammedina.biblioteca.infrastructure.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.williammedina.biblioteca.domain.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Slf4j
@Service
public class TokenService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    public String generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
            return JWT.create()
                    .withIssuer("Biblioteca")
                    .withSubject(user.getEmail())
                    .withClaim("id", user.getId())
                    .withExpiresAt(generateExpirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            log.error("Error generating token for user {}: {}", user.getEmail(), exception.getMessage());
            throw new RuntimeException("Error al generar el token", exception);
        }
    }

    public String getSubjectFromToken(String token) {
        if (token == null) {
            log.warn("Token is null. Cannot extract subject.");
            throw new IllegalArgumentException("El token no puede ser nulo");
        }

        DecodedJWT decodedJWT = null;
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
            decodedJWT = JWT.require(algorithm)
                    .withIssuer("Biblioteca")
                    .build()
                    .verify(token);
        } catch (JWTVerificationException exception) {
            log.warn("Token verification failed: {}", exception.getMessage());
            //throw new RuntimeException("Token inválido: " + exception.getMessage(), exception);
            throw exception;
        }

        if (decodedJWT == null || decodedJWT.getSubject() == null) {
            log.error("Token is invalid: Subject is missing.");
            throw new RuntimeException("Token inválido: El campo 'sujeto' no está presente");
        }
        return decodedJWT.getSubject();
    }

    private Instant generateExpirationDate() {
        return LocalDateTime.now().plusDays(30).toInstant(ZoneOffset.of("-05:00"));
    }
}
