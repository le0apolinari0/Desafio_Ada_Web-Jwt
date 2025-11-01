package br.com.ada.ConsoleApp.service;

import br.com.ada.ConsoleApp.domain.Todo;
import br.com.ada.ConsoleApp.dto.response.TodoResponse;

import java.util.concurrent.CompletableFuture;

public interface TodoService {
    CompletableFuture<TodoResponse> getTodos(int limit, int skip);
    CompletableFuture<Todo> addTodo(Todo todo);
    CompletableFuture<Todo> updateTodo(Long id, Todo todo);
    CompletableFuture<Todo> deleteTodo(Long id);
}
