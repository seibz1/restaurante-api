package br.com.seib.restaurante.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidade que representa a tabela 'grupos_usuarios' no banco de dados.
 * É essencial para o controle de acesso e permissões (requisito obrigatório).
 */
@Data // (Lombok) Gera automaticamente getters, setters, toString(), etc.
@NoArgsConstructor // (Lombok) Construtor vazio, exigido pelo JPA.
@AllArgsConstructor // (Lombok) Construtor com todos os argumentos.
@Entity(name = "grupos_usuarios") // Mapeia a classe para a tabela no MySQL.
public class GrupoUsuario {

    /**
     * Chave Primária (Primary Key) da tabela.
     * O valor é gerado automaticamente pelo MySQL (AUTO_INCREMENT).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nome do grupo de usuário (Ex: "ADMIN", "GERENTE", "CLIENTE").
     * A restrição UNIQUE garante que não haja grupos duplicados.
     */
    @Column(nullable = false, unique = true)
    private String nome;

}