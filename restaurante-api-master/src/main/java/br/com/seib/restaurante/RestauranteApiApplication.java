package br.com.seib.restaurante;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Ponto de entrada (Entry Point) da aplicação Spring Boot.
 * Esta classe é responsável por iniciar o servidor e o contexto da aplicação.
 */
@SpringBootApplication
// Esta é a anotação mais importante do Spring Boot. Ela combina:
// 1. @Configuration: Define a classe como fonte de beans de configuração.
// 2. @EnableAutoConfiguration: Habilita a configuração automática (ex: configura MySQL, MongoDB, Servidor Web).
// 3. @ComponentScan: Faz a varredura de componentes (@Controller, @Service, @Repository)
//    a partir deste pacote base (br.com.seib.restaurante).
public class RestauranteApiApplication {

    public static void main(String[] args) {
        // Método padrão que executa a aplicação Spring Boot, iniciando o servidor embutido (Tomcat).
        SpringApplication.run(RestauranteApiApplication.class, args);
    }

}