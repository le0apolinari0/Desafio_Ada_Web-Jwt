package br.com.ada.ConsoleApp;

import br.com.ada.ConsoleApp.client.ApiClient;
import br.com.ada.ConsoleApp.config.AppConfig;
import br.com.ada.ConsoleApp.menu.ConsoleMenu;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class ConsoleAppApplication {

    public static void main(String[] args) {
        try {
            AppConfig config = new AppConfig();
            ConsoleMenu menu = config.consoleMenu();

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                log.info("Aplicação finalizada");
            }));

            menu.start();

        } catch (Exception e) {
            System.err.println("💥 Erro crítico ao iniciar a aplicação: " + e.getMessage());
            log.error("Critical error starting application", e);
            System.exit(1);
        }
    }
}

