package br.com.ada.ConsoleApp.formatter;

import br.com.ada.ConsoleApp.domain.Product;
import br.com.ada.ConsoleApp.domain.Todo;

import java.util.List;

public class ConsoleFormatter {

    // Cores ANSI
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String BOLD = "\u001B[1m";

    public void printHeader(String title) {
        System.out.println();
        System.out.println(CYAN + "┌" + "─".repeat(78) + "┐" + RESET);
        System.out.println(CYAN + "│" + BOLD + centerString(title, 78) + RESET + CYAN + "│" + RESET);
        System.out.println(CYAN + "└" + "─".repeat(78) + "┘" + RESET);
        System.out.println();
    }

    public void printProducts(List<Product> products) {
        if (products.isEmpty()) {
            System.out.println(YELLOW + "📭 Nenhum produto encontrado" + RESET);
            return;
        }

        System.out.println(BOLD + BLUE + "┌─────┬──────────────────────────────────┬────────────────────────────┬────────────┬────────┐" + RESET);
        System.out.println(BOLD + BLUE + "│ " + CYAN + "ID  " + BLUE + "│ " + CYAN + "Título" +
                " ".repeat(26) + BLUE + "│ " + CYAN + "Categoria" +
                " ".repeat(15) + BLUE + "│ " + CYAN + "Preço" +
                " ".repeat(6) + BLUE + "│ " + CYAN + "Avaliação" + BLUE + " │" + RESET);
        System.out.println(BOLD + BLUE + "├─────┼──────────────────────────────────┼────────────────────────────┼────────────┼────────┤" + RESET);

        for (Product product : products) {
            String title = truncate(product.getTitle(), 30);
            String category = truncate(product.getCategory(), 24);
            String price = String.format("$%.2f", product.getPrice());
            String rating = String.format("%.1f ⭐", product.getRating());

            System.out.printf(BLUE + "│ " + RESET + "%-4d" + BLUE + "│ " + RESET + "%-30s" +
                            BLUE + "│ " + RESET + "%-24s" + BLUE + "│ " +
                            GREEN + "%-10s" + BLUE + "│ " + YELLOW + "%-8s" + BLUE + " │\n" + RESET,
                    product.getId(), title, category, price, rating);
        }

        System.out.println(BOLD + BLUE + "└─────┴──────────────────────────────────┴────────────────────────────┴────────────┴────────┘" + RESET);
    }

    public void printTodos(List<Todo> todos) {
        if (todos.isEmpty()) {
            System.out.println(YELLOW + "📭 Nenhuma tarefa encontrada" + RESET);
            return;
        }

        System.out.println(BOLD + PURPLE + "┌─────┬──────────────────────────────────────────────────────────────────┬─────────────┐" + RESET);
        System.out.println(BOLD + PURPLE + "│ " + CYAN + "ID  " + PURPLE + "│ " + CYAN + "Tarefa" +
                " ".repeat(58) + PURPLE + "│ " + CYAN + "Status" +
                " ".repeat(5) + PURPLE + " │" + RESET);
        System.out.println(BOLD + PURPLE + "├─────┼──────────────────────────────────────────────────────────────────┼─────────────┤" + RESET);

        for (Todo todo : todos) {
            String task = truncate(todo.getTodo(), 60);
            String status = todo.getCompleted() ?
                    GREEN + "✅ Concluído" + RESET : RED + "⏳ Pendente" + RESET;

            System.out.printf(PURPLE + "│ " + RESET + "%-4d" + PURPLE + "│ " + RESET + "%-60s" +
                            PURPLE + "│ " + RESET + "%-11s" + PURPLE + " │\n" + RESET,
                    todo.getId(), task, status);
        }

        System.out.println(BOLD + PURPLE + "└─────┴──────────────────────────────────────────────────────────────────┴─────────────┘" + RESET);
    }

    public void printSuccess(String message) {
        System.out.println(GREEN + "✅ " + message + RESET);
    }

    public void printError(String message) {
        System.out.println(RED + "❌ " + message + RESET);
    }

    public void printInfo(String message) {
        System.out.println(BLUE + "ℹ️  " + message + RESET);
    }

    public void printWarning(String message) {
        System.out.println(YELLOW + "⚠️  " + message + RESET);
    }

    private String centerString(String text, int width) {
        if (text.length() >= width) {
            return text;
        }
        int padding = (width - text.length()) / 2;
        return " ".repeat(padding) + text + " ".repeat(width - text.length() - padding);
    }

    private String truncate(String text, int maxLength) {
        if (text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength - 3) + "...";
    }
}
