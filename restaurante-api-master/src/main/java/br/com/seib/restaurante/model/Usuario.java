package br.com.seib.restaurante.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidade que representa a tabela 'usuarios', contendo as informações básicas
 * dos usuários do sistema e o mapeamento de permissões.
 */
@Data // (Lombok) Gera getters, setters, etc.
@NoArgsConstructor // (Lombok) Construtor vazio.
@AllArgsConstructor // (Lombok) Construtor com todos os campos.
@Entity(name = "usuarios") // Mapeia a classe para a tabela no MySQL.
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Chave Primária

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email; // Restrição UNIQUE para o login

    @Column(nullable = false)
    private String senha;

    /**
     * RELACIONAMENTO CHAVE ESTRANGEIRA (Foreign Key):
     * Mapeia o relacionamento N:1 (Muitos Usuários para Um Grupo).
     * Essa é a coluna 'grupo_id' que liga esta tabela à tabela 'grupos_usuarios'.
     */
    @ManyToOne // (JPA) Muitos Usuários para Um GrupoUsuario
    @JoinColumn(name = "grupo_id", nullable = false) // Coluna 'grupo_id' com restrição NOT NULL
    private GrupoUsuario grupo;

}