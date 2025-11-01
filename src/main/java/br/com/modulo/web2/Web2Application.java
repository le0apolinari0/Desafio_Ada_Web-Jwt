package br.com.modulo.web2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableJpaAuditing
@EnableCaching
@EnableAsync
@SpringBootApplication
public class Web2Application {

	public static void main(String[] args) {
		SpringApplication.run(Web2Application.class, args);
	}

}
