package br.com.modulo.web2.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "Login é obrigatório (email, CPF ou telefone)")
    private String login;

    @NotBlank(message = "Senha é obrigatória")
    private String password;
}


