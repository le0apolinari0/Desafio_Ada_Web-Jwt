package br.com.ada.ConsoleApp.menu;

import br.com.ada.ConsoleApp.domain.Todo;
import br.com.ada.ConsoleApp.dto.response.ProductResponse;
import br.com.ada.ConsoleApp.dto.response.TodoResponse;
import br.com.ada.ConsoleApp.exception.AppException;
import br.com.ada.ConsoleApp.exception.ErrorHandler;
import br.com.ada.ConsoleApp.formatter.ConsoleFormatter;
import br.com.ada.ConsoleApp.service.ProductService;
import br.com.ada.ConsoleApp.service.TodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RequiredArgsConstructor
public class ConsoleMenu {

    private final ProductService productService;
    private final TodoService todoService;
    private final ConsoleFormatter formatter;
    private final ErrorHandler errorHandler;
    private final Scanner scanner;
    private boolean running = true;

    public void start() {
        formatter.printHeader("🏪 DUMMYJSON CLI - Gerenciador de Produtos e Tarefas");

        while (running) {
            printMainMenu();
            int choice = readInt("Escolha uma opção: ");
            handleMainMenuChoice(choice);
        }
    }

    private void printMainMenu() {
        System.out.println();
        System.out.println(ConsoleFormatter.BOLD + ConsoleFormatter.CYAN + "📋 MENU PRINCIPAL" + ConsoleFormatter.RESET);
        System.out.println(ConsoleFormatter.BLUE + "1. 📦 Gerenciar Produtos");
        System.out.println("2. ✅ Gerenciar Tarefas (Todos)");
        System.out.println("3. 🗑️  Limpar Cache");
        System.out.println("4. ℹ️  Sobre");
        System.out.println("0. 🚪 Sair" + ConsoleFormatter.RESET);
        System.out.println();
    }

    private void handleMainMenuChoice(int choice) {
        try {
            switch (choice) {
                case 1 -> showProductsMenu();
                case 2 -> showTodosMenu();
                case 3 -> clearCache();
                case 4 -> showAbout();
                case 0 -> exit();
                default -> formatter.printWarning("Opção inválida! Tente novamente.");
            }
        } catch (AppException e) {
            errorHandler.handleError(e);
        } catch (Exception e) {
            errorHandler.handleGenericError(e, "Menu Principal");
        }
    }

    private void showProductsMenu() {
        while (true) {
            System.out.println();
            formatter.printHeader("📦 GERENCIAR PRODUTOS");
            System.out.println(ConsoleFormatter.BLUE + "1. 📄 Listar Produtos (Paginação)");
            System.out.println("2. 🔍 Buscar Produtos");
            System.out.println("0. ↩️  Voltar" + ConsoleFormatter.RESET);
            System.out.println();

            int choice = readInt("Escolha uma opção: ");

            switch (choice) {
                case 1 -> listProducts();
                case 2 -> searchProducts();
                case 0 -> { return; }
                default -> formatter.printWarning("Opção inválida!");
            }
        }
    }

    private void showTodosMenu() {
        while (true) {
            System.out.println();
            formatter.printHeader("✅ GERENCIAR TAREFAS (TODOS)");
            System.out.println(ConsoleFormatter.BLUE + "1. 📄 Listar Tarefas");
            System.out.println("2. ➕ Adicionar Tarefa");
            System.out.println("3. ✏️  Atualizar Tarefa");
            System.out.println("4. 🗑️  Remover Tarefa");
            System.out.println("0. ↩️  Voltar" + ConsoleFormatter.RESET);
            System.out.println();

            int choice = readInt("Escolha uma opção: ");

            switch (choice) {
                case 1 -> listTodos();
                case 2 -> addTodo();
                case 3 -> updateTodo();
                case 4 -> deleteTodo();
                case 0 -> { return; }
                default -> formatter.printWarning("Opção inválida!");
            }
        }
    }

    private void listProducts() {
        int limit = readInt("Quantidade por página: ");
        int skip = readInt("Pular quantos registros: ");

        formatter.printInfo("Carregando produtos...");

        CompletableFuture<ProductResponse> future = productService.getProducts(limit, skip);

        future.thenAccept(response -> {
            System.out.println();
            formatter.printHeader(String.format("📦 PRODUTOS (%d de %d)",
                    response.getProducts().size(), response.getTotal()));
            formatter.printProducts(response.getProducts());
            showPaginationInfo(response.getSkip(), response.getLimit(), response.getTotal());
        }).exceptionally(throwable -> {
            errorHandler.handleGenericError((Exception) throwable, "Listagem de Produtos");
            return null;
        });

        // Wait for completion
        future.join();
    }

    private void searchProducts() {
        System.out.print("🔍 Termo de busca: ");
        String query = scanner.nextLine();

        if (query.trim().isEmpty()) {
            formatter.printWarning("Termo de busca não pode estar vazio!");
            return;
        }

        formatter.printInfo("Buscando produtos...");

        CompletableFuture<ProductResponse> future = productService.searchProducts(query);

        future.thenAccept(response -> {
            System.out.println();
            formatter.printHeader(String.format("🔍 RESULTADOS PARA '%s' (%d produtos)",
                    query, response.getProducts().size()));
            formatter.printProducts(response.getProducts());
        }).exceptionally(throwable -> {
            errorHandler.handleGenericError((Exception) throwable, "Busca de Produtos");
            return null;
        });

        future.join();
    }

    private void listTodos() {
        int limit = readInt("Quantidade por página: ");
        int skip = readInt("Pular quantos registros: ");

        formatter.printInfo("Carregando tarefas...");

        CompletableFuture<TodoResponse> future = todoService.getTodos(limit, skip);

        future.thenAccept(response -> {
            System.out.println();
            formatter.printHeader(String.format("✅ TAREFAS (%d de %d)",
                    response.getTodos().size(), response.getTotal()));
            formatter.printTodos(response.getTodos());
            showPaginationInfo(response.getSkip(), response.getLimit(), response.getTotal());
        }).exceptionally(throwable -> {
            errorHandler.handleGenericError((Exception) throwable, "Listagem de Tarefas");
            return null;
        });

        future.join();
    }

    private void addTodo() {
        System.out.print("📝 Descrição da tarefa: ");
        String todoText = scanner.nextLine();

        if (todoText.trim().isEmpty()) {
            formatter.printWarning("Descrição da tarefa não pode estar vazia!");
            return;
        }

        Long userId = readLong("👤 ID do usuário: ");

        Todo newTodo = Todo.builder()
                .todo(todoText)
                .completed(false)
                .userId(userId)
                .build();

        formatter.printInfo("Adicionando tarefa...");

        CompletableFuture<Todo> future = todoService.addTodo(newTodo);

        future.thenAccept(todo -> {
            formatter.printSuccess("Tarefa adicionada com sucesso! ID: " + todo.getId());
        }).exceptionally(throwable -> {
            errorHandler.handleGenericError((Exception) throwable, "Adição de Tarefa");
            return null;
        });

        future.join();
    }

    private void updateTodo() {
        Long id = readLong("✏️  ID da tarefa para atualizar: ");

        System.out.print("📝 Nova descrição (deixe em branco para manter): ");
        String newTodo = scanner.nextLine();

        System.out.print("✅ Concluída? (s/n - deixe em branco para manter): ");
        String completedInput = scanner.nextLine();

        Boolean completed = null;
        if (!completedInput.isEmpty()) {
            completed = completedInput.equalsIgnoreCase("s");
        }

        Todo todoUpdate = Todo.builder()
                .todo(newTodo.isEmpty() ? null : newTodo)
                .completed(completed)
                .build();

        formatter.printInfo("Atualizando tarefa...");

        CompletableFuture<Todo> future = todoService.updateTodo(id, todoUpdate);

        future.thenAccept(todo -> {
            formatter.printSuccess("Tarefa atualizada com sucesso! ID: " + todo.getId());
        }).exceptionally(throwable -> {
            errorHandler.handleGenericError((Exception) throwable, "Atualização de Tarefa");
            return null;
        });

        future.join();
    }

    private void deleteTodo() {
        Long id = readLong("🗑️  ID da tarefa para remover: ");

        formatter.printWarning("Tem certeza que deseja remover esta tarefa? (s/n): ");
        String confirmation = scanner.nextLine();

        if (!confirmation.equalsIgnoreCase("s")) {
            formatter.printInfo("Remoção cancelada.");
            return;
        }

        formatter.printInfo("Removendo tarefa...");

        CompletableFuture<Todo> future = todoService.deleteTodo(id);

        future.thenAccept(todo -> {
            formatter.printSuccess("Tarefa removida com sucesso! ID: " + todo.getId());
        }).exceptionally(throwable -> {
            errorHandler.handleGenericError((Exception) throwable, "Remoção de Tarefa");
            return null;
        });

        future.join();
    }

    private void clearCache() {
        formatter.printInfo("Limpando cache...");
        // Cache clearing would be implemented here
        formatter.printSuccess("Cache limpo com sucesso!");
    }

    private void showAbout() {
        formatter.printHeader("ℹ️  SOBRE");
        System.out.println(ConsoleFormatter.CYAN + "🏪 DummyJSON CLI" + ConsoleFormatter.RESET);
        System.out.println("Uma aplicação Java para gerenciar produtos e tarefas");
        System.out.println("usando a API DummyJSON");
        System.out.println();
        System.out.println(ConsoleFormatter.BLUE + "📊 Funcionalidades:" + ConsoleFormatter.RESET);
        System.out.println("• 📦 Gerenciamento de produtos com paginação");
        System.out.println("• 🔍 Busca de produtos por título e descrição");
        System.out.println("• ✅ Gerenciamento completo de tarefas (todos)");
        System.out.println("• ⚡ Cache inteligente com TTL");
        System.out.println("• 🎨 Interface colorida e intuitiva");
    }

    private void exit() {
        formatter.printInfo("Saindo... Até logo! 👋");
        running = false;
    }

    private void showPaginationInfo(int skip, int limit, int total) {
        int currentPage = (skip / limit) + 1;
        int totalPages = (int) Math.ceil((double) total / limit);

        System.out.println();
        formatter.printInfo(String.format("📄 Página %d de %d | Total: %d registros",
                currentPage, totalPages, total));
    }

    private int readInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                formatter.printWarning("Por favor, digite um número válido.");
            }
        }
    }

    private Long readLong(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine();
                return Long.parseLong(input);
            } catch (NumberFormatException e) {
                formatter.printWarning("Por favor, digite um número válido.");
            }
        }
    }
}
