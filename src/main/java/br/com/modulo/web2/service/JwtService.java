package br.com.modulo.web2.service;


import org.springframework.security.core.Authentication;

public interface JwtService {
    String generateToken(Authentication authentication);
    String extractUsername(String token);
    boolean isTokenValid(String token);
}
