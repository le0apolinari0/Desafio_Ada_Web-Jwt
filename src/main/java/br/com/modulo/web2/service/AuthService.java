package br.com.modulo.web2.service;

import br.com.modulo.web2.dto.request.*;
import br.com.modulo.web2.dto.response.AuthResponse;
import br.com.modulo.web2.dto.response.UserResponse;

public interface AuthService {
    AuthResponse authenticate(LoginRequest request);
    UserResponse register(UserRequest request);
}
