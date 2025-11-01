package br.com.modulo.web2.execption.errosApi;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String message) {
        super(message);
    }
}


