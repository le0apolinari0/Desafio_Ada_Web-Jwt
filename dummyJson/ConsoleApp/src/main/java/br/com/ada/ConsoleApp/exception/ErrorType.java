package br.com.ada.ConsoleApp.exception;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorType {
    NETWORK_ERROR("Erro de conexão com a API"),
    API_ERROR("Erro retornado pela API"),
    VALIDATION_ERROR("Erro de validação"),
    CACHE_ERROR("Erro no cache"),
    CONFIG_ERROR("Erro de configuração");

    private final String description;
}

