package br.com.ada.ConsoleApp.exception;


import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ErrorHandler {

    private static final String RED = "\u001B[31m";
    private static final String RESET = "\u001B[0m";
    private static final String YELLOW = "\u001B[33m";

    public void handleError(AppException e) {
        String errorMessage = String.format(
                "🚨 %s%s%s - %s\n📝 Detalhes: %s",
                RED, e.getErrorType().getDescription(), RESET,
                e.getMessage(),
                e.getDetails()
        );

        System.err.println(errorMessage);
        log.error("AppException: {}", e.getMessage(), e);
    }

    public void handleGenericError(Exception e, String context) {
        String errorMessage = String.format(
                "💥 %sErro inesperado%s em %s: %s",
                RED, RESET, context, e.getMessage()
        );

        System.err.println(errorMessage);
        log.error("Generic error in {}: {}", context, e.getMessage(), e);
    }
}
