package br.com.modulo.web2.execption.errosNegocio;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ErrorResponse {
    private String mensagem;
    private String codigo;
    private String detalhes;
    private LocalDateTime timestamp;
    private String trace;

    public ErrorResponse(
            String mensagem,
            String codigo,
            String detalhes) {
        this.mensagem = mensagem;
        this.codigo = codigo;
        this.detalhes = detalhes;
    }
}
