package br.com.modulo.web2.service.impl;

import br.com.modulo.web2.dto.request.LoginRequest;
import br.com.modulo.web2.dto.request.UserRequest;
import br.com.modulo.web2.dto.response.AuthResponse;
import br.com.modulo.web2.dto.response.UserResponse;
import br.com.modulo.web2.entity.Usuario;
import br.com.modulo.web2.execption.errosApi.EmailNotVerifiedException;
import br.com.modulo.web2.execption.errosApi.InvalidCredentialsException;
import br.com.modulo.web2.execption.errosApi.UserNotFoundException;
import br.com.modulo.web2.repository.UserRepository;
import br.com.modulo.web2.service.JwtService;
import br.com.modulo.web2.service.AuthService;
import br.com.modulo.web2.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final UserService userService;

    @Override
    public AuthResponse authenticate(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getLogin(), request.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            Usuario user = userRepository.findByEmailOrCpfOrTelefone(request.getLogin())
                    .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));

            if (!user.getEmailVerified()) {
                throw new EmailNotVerifiedException("Email não verificado. Por favor, verifique seu email.");
            }

            String jwt = jwtService.generateToken(authentication);

            return AuthResponse.builder()
                    .token(jwt)
                    .type("Bearer")
                    .id(user.getId())
                    .nome(user.getNome())
                    .email(user.getEmail())
                    .cpf(user.getCpf())
                    .telefone(user.getTelefone())
                    .roles(user.getRoles().stream()
                            .map(role -> role.getName().name())
                            .collect(java.util.stream.Collectors.toSet()))
                    .build();

        } catch (BadCredentialsException e) {
            throw new InvalidCredentialsException("Credenciais inválidas");
        }
    }

    @Override
    public UserResponse register(UserRequest request) {
        return userService.createUser(request);
    }
}

