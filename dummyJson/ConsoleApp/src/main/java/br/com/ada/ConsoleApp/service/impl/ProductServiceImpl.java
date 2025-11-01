package br.com.ada.ConsoleApp.service.impl;

import br.com.ada.ConsoleApp.cache.CacheManager;
import br.com.ada.ConsoleApp.client.ApiClient;
import br.com.ada.ConsoleApp.domain.Product;
import br.com.ada.ConsoleApp.dto.response.ProductResponse;
import br.com.ada.ConsoleApp.dto.response.SearchtResponse;
import br.com.ada.ConsoleApp.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ApiClient apiClient;
    private final CacheManager cacheManager;
    private static final String CACHE_PREFIX = "products:";

    @Override
    public CompletableFuture<ProductResponse> getProducts(int limit, int skip) {
        String cacheKey = CACHE_PREFIX + "list:" + limit + ":" + skip;

        ProductResponse cached = cacheManager.get(cacheKey);
        if (cached != null) {
            log.info("📦 Retornando produtos do cache: {} produtos", cached.getProducts().size());
            return CompletableFuture.completedFuture(cached);
        }

        String endpoint = String.format("/products?limit=%d&skip=%d", limit, skip);

        return apiClient.get(endpoint, ProductResponse.class)
                .thenApply(response -> {
                    cacheManager.put(cacheKey, response, 300); // 5 minutos
                    log.info("✅ Produtos carregados da API: {} produtos", response.getProducts().size());
                    return response;
                });
    }

    @Override
    public CompletableFuture<ProductResponse> searchProducts(String query) {
        String cacheKey = CACHE_PREFIX + "search:" + query;

        ProductResponse cached = cacheManager.get(cacheKey);
        if (cached != null) {
            log.info("🔍 Retornando busca do cache: '{}' - {} produtos", query, cached.getProducts().size());
            return CompletableFuture.completedFuture(cached);
        }

        String endpoint = String.format("/products/search?q=%s", query);

        return apiClient.get(endpoint, ProductResponse.class)
                .thenApply(response -> {
                    cacheManager.put(cacheKey, response, 300); // 5 minutos
                    log.info("✅ Busca concluída: '{}' - {} produtos", query, response.getProducts().size());
                    return response;
                });
    }
}
