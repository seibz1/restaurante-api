package br.com.seib.restaurante.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuração Web MVC para habilitar o CORS (Cross-Origin Resource Sharing).
 * Essencial para permitir que o frontend (rodando localmente via file:// ou outra porta)
 * se comunique com a API REST.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Mapeia para todos os endpoints (/**)
        registry.addMapping("/**")
                // Permite requisições de qualquer origem (ideal para o ambiente de desenvolvimento)
                .allowedOrigins("*")
                // Permite todos os métodos HTTP necessários
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                // Permite todos os headers
                .allowedHeaders("*");
    }
}