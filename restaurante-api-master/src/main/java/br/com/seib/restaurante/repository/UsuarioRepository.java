package br.com.seib.restaurante.repository;

import br.com.seib.restaurante.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Interface Repository para a Entidade Usuario.
 * Herda funcionalidades CRUD do JpaRepository para acesso à tabela 'usuarios'.
 */
@Repository // Identifica a interface como um repositório gerenciado pelo Spring.
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // JpaRepository<Usuario, Long>
    // 1. Usuario: Entidade gerenciada.
    // 2. Long: Tipo da Chave Primária (PK).

    /**
     * Exemplo de Query Derivation: O Spring Data JPA gera automaticamente a consulta SQL
     * (SELECT * FROM usuarios WHERE email = ?) a partir do nome do método.
     * * Retorna Optional<Usuario> para tratar de forma segura o caso em que o e-mail não é encontrado.
     */
    Optional<Usuario> findByEmail(String email);
}