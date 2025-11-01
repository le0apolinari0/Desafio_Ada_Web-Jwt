package br.com.ada.ConsoleApp.service;


import br.com.ada.ConsoleApp.dto.response.ProductResponse;


import java.util.concurrent.CompletableFuture;

public interface ProductService {
    CompletableFuture<ProductResponse> getProducts(int limit, int skip);
    CompletableFuture<ProductResponse> searchProducts(String query);
}

