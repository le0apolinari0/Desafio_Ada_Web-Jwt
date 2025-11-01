package br.com.modulo.web2.dto.response;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@Builder
public class UserResponse {
    private Long id;
    private String nome;
    private String email;
    private String cpf;
    private String telefone;
    private Boolean active;
    private Boolean emailVerified;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Set<String> roles;
    private List<AddressResponse> enderecos;
}


