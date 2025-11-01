package br.com.ada.ConsoleApp.client;

import br.com.ada.ConsoleApp.exception.AppException;
import br.com.ada.ConsoleApp.exception.ErrorType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RequiredArgsConstructor
public class ApiClient {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String baseUrl;
    private final Duration timeout;

    public <T> CompletableFuture<T> get(String endpoint, Class<T> responseType) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + endpoint))
                .timeout(timeout)
                .header("Content-Type", "application/json")
                .GET()
                .build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> handleResponse(response, responseType));
    }

    public <T, R> CompletableFuture<T> post(String endpoint, R body, Class<T> responseType) {
        try {
            String bodyJson = objectMapper.writeValueAsString(body);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + endpoint))
                    .timeout(timeout)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(bodyJson))
                    .build();

            return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(response -> handleResponse(response, responseType));
        } catch (Exception e) {
            throw new AppException(ErrorType.API_ERROR, "Erro ao criar requisição POST", e.getMessage(), e);
        }
    }

    public <T, R> CompletableFuture<T> put(String endpoint, R body, Class<T> responseType) {
        try {
            String bodyJson = objectMapper.writeValueAsString(body);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + endpoint))
                    .timeout(timeout)
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(bodyJson))
                    .build();

            return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(response -> handleResponse(response, responseType));
        } catch (Exception e) {
            throw new AppException(ErrorType.API_ERROR, "Erro ao criar requisição PUT", e.getMessage(), e);
        }
    }

    public <T> CompletableFuture<T> delete(String endpoint, Class<T> responseType) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + endpoint))
                .timeout(timeout)
                .header("Content-Type", "application/json")
                .DELETE()
                .build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> handleResponse(response, responseType));
    }

    private <T> T handleResponse(HttpResponse<String> response, Class<T> responseType) {
        try {
            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                return objectMapper.readValue(response.body(), responseType);
            } else {
                throw new AppException(
                        ErrorType.API_ERROR,
                        "Erro na resposta da API",
                        String.format("Status: %d, Body: %s", response.statusCode(), response.body())
                );
            }
        } catch (Exception e) {
            if (e instanceof AppException) {
                throw (AppException) e;
            }
            throw new AppException(ErrorType.API_ERROR, "Erro ao processar resposta", e.getMessage(), e);
        }
    }
}
