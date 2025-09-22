package br.ifsp.contacts.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI contactsOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Contacts API")
                        .description("API REST para gerenciamento de contatos e endereços")
                        .version("1.0.0"));
    }
}
