package br.com.modulo.web2.execption.errosNegocio;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BusinessException extends RuntimeException {

    @ResponseStatus(HttpStatus.OK)
    public static class Success200Exception extends RuntimeException {
        public Success200Exception(String message) {
            super(message);
        }
    }
    @ResponseStatus(HttpStatus.CREATED)
    public static class Success201Exception extends RuntimeException {
        public Success201Exception(String message) {
            super(message);
        }
    }

}
