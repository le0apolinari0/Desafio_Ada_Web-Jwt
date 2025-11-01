package br.com.modulo.web2.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Sistema Web2 - API")
                        .description("Sistema de CRUD com autenticação JWT")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Suporte")
                                .email("suporte@web2.com")));
    }
}
