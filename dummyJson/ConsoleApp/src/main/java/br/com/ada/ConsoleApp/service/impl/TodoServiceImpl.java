package br.com.ada.ConsoleApp.service.impl;

import br.com.ada.ConsoleApp.cache.CacheManager;
import br.com.ada.ConsoleApp.client.ApiClient;
import br.com.ada.ConsoleApp.domain.Todo;
import br.com.ada.ConsoleApp.dto.request.TodoRequest;
import br.com.ada.ConsoleApp.dto.response.TodoResponse;
import br.com.ada.ConsoleApp.service.TodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;

@Slf4j
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService {

    private final ApiClient apiClient;
    private final CacheManager cacheManager;
    private static final String CACHE_PREFIX = "todos:";

    @Override
    public CompletableFuture<TodoResponse> getTodos(int limit, int skip) {
        String cacheKey = CACHE_PREFIX + "list:" + limit + ":" + skip;

        TodoResponse cached = cacheManager.get(cacheKey);
        if (cached != null) {
            log.info("✅ Retornando todos do cache: {} tarefas", cached.getTodos().size());
            return CompletableFuture.completedFuture(cached);
        }

        String endpoint = String.format("/todos?limit=%d&skip=%d", limit, skip);

        return apiClient.get(endpoint, TodoResponse.class)
                .thenApply(response -> {
                    cacheManager.put(cacheKey, response, 300); // 5 minutos
                    log.info("✅ Todos carregados da API: {} tarefas", response.getTodos().size());
                    return response;
                });
    }

    @Override
    public CompletableFuture<Todo> addTodo(Todo todo) {
        TodoRequest request = TodoRequest.builder()
                .todo(todo.getTodo())
                .completed(todo.getCompleted())
                .userId(todo.getUserId())
                .build();

        // Invalida cache de lista
        invalidateListCache();

        return apiClient.post("/todos/add", request, Todo.class)
                .thenApply(response -> {
                    log.info("➕ Todo adicionado: ID {}", response.getId());
                    return response;
                });
    }

    @Override
    public CompletableFuture<Todo> updateTodo(Long id, Todo todo) {
        TodoRequest request = TodoRequest.builder()
                .todo(todo.getTodo())
                .completed(todo.getCompleted())
                .userId(todo.getUserId())
                .build();

        // Invalida cache
        invalidateListCache();
        cacheManager.remove(CACHE_PREFIX + "todo:" + id);

        return apiClient.put("/todos/" + id, request, Todo.class)
                .thenApply(response -> {
                    log.info("✏️ Todo atualizado: ID {}", response.getId());
                    return response;
                });
    }

    @Override
    public CompletableFuture<Todo> deleteTodo(Long id) {
        // Invalida cache
        invalidateListCache();
        cacheManager.remove(CACHE_PREFIX + "todo:" + id);

        return apiClient.delete("/todos/" + id, Todo.class)
                .thenApply(response -> {
                    log.info("🗑️ Todo removido: ID {}", response.getId());
                    return response;
                });
    }

    private void invalidateListCache() {
        cacheManager.clear(); // Simplificado - poderia ser mais específico
        log.debug("🔄 Cache de todos invalidado");
    }
}
