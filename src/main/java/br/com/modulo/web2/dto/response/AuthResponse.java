package br.com.modulo.web2.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class AuthResponse {
    private String token;
    private String type;
    private Long id;
    private String nome;
    private String email;
    private String cpf;
    private String telefone;
    private Set<String> roles;
}


