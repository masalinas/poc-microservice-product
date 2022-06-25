package io.oferto.microservice;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Product API", version = "1.0", description = "Product Microservice API Information"))
public class MicroserviceProductApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceProductApplication.class, args);
	}

    @Bean
    ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
