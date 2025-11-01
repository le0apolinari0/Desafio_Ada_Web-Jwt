package br.com.modulo.web2.execption.errosApi;


public class TokenExpiredException extends RuntimeException {
    public TokenExpiredException(String message) {
        super(message);
    }
}


