package br.com.seib.restaurante.repository;

import br.com.seib.restaurante.model.GrupoUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Interface Repository para a Entidade GrupoUsuario.
 * Herda funcionalidades CRUD do JpaRepository.
 */
@Repository // Identifica a interface como um repositório gerenciado pelo Spring.
public interface GrupoUsuarioRepository extends JpaRepository<GrupoUsuario, Long> {

    // JpaRepository<GrupoUsuario, Long>
    // 1. GrupoUsuario: Entidade gerenciada.
    // 2. Long: Tipo da Chave Primária (PK).

    /**
     * Exemplo de Query Derivation: O Spring Data JPA traduz automaticamente
     * o nome do método para a consulta SQL: "SELECT * FROM grupos_usuarios WHERE nome = ?"
     */
    GrupoUsuario findByNome(String nome);
}