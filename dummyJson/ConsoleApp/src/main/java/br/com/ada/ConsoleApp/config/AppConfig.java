package br.com.ada.ConsoleApp.config;


import br.com.ada.ConsoleApp.cache.CacheManager;
import br.com.ada.ConsoleApp.client.ApiClient;
import br.com.ada.ConsoleApp.exception.ErrorHandler;
import br.com.ada.ConsoleApp.formatter.ConsoleFormatter;
import br.com.ada.ConsoleApp.menu.ConsoleMenu;
import br.com.ada.ConsoleApp.service.ProductService;
import br.com.ada.ConsoleApp.service.TodoService;
import br.com.ada.ConsoleApp.service.impl.ProductServiceImpl;
import br.com.ada.ConsoleApp.service.impl.TodoServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpClient;
import java.time.Duration;
import java.util.Properties;
import java.util.Scanner;


@Slf4j
public class AppConfig {

    private final Properties properties;

    public AppConfig() {
        this.properties = loadProperties();
    }

    public ConsoleMenu consoleMenu() {
        return new ConsoleMenu(
                productService(),
                todoService(),
                consoleFormatter(),
                errorHandler(),
                new Scanner(System.in)
        );
    }

    public ProductService productService() {
        return new ProductServiceImpl(apiClient(), cacheManager());
    }

    public TodoService todoService() {
        return new TodoServiceImpl(apiClient(), cacheManager());
    }

    public ApiClient apiClient() {
        return new ApiClient(
                httpClient(),
                objectMapper(),
                getBaseUrl(),
                getTimeout()
        );
    }

    public CacheManager cacheManager() {
        long ttl = Long.parseLong(properties.getProperty("cache.ttl.seconds", "300"));
        return new CacheManager(ttl);
    }

    public ConsoleFormatter consoleFormatter() {
        return new ConsoleFormatter();
    }

    public ErrorHandler errorHandler() {
        return new ErrorHandler();
    }

    private HttpClient httpClient() {
        return HttpClient.newBuilder()
                .connectTimeout(getTimeout())
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
    }

    private ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new SimpleModule());
        return mapper;
    }

    private String getBaseUrl() {
        return properties.getProperty("api.base.url", "https://dummyjson.com");
    }

    private Duration getTimeout() {
        int timeoutSeconds = Integer.parseInt(properties.getProperty("http.timeout.seconds", "30"));
        return Duration.ofSeconds(timeoutSeconds);
    }

    private Properties loadProperties() {
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (input != null) {
                props.load(input);
            } else {
                log.warn("application.properties not found, using default values");
            }
        } catch (IOException e) {
            log.error("Error loading application.properties: {}", e.getMessage());
        }
        return props;
    }
}
