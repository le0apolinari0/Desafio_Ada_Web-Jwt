package br.com.modulo.web2.dto.response;

import br.com.modulo.web2.entity.Address;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AddressResponse {
    private Long id;
    private String logradouro;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;
    private Address.AddressType tipo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
