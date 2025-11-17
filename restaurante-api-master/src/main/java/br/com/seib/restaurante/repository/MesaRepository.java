package br.com.seib.restaurante.repository;

import br.com.seib.restaurante.model.Mesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository; // Adicionamos a anotação padrão

/**
 * Interface Repository para a Entidade Mesa.
 * Herda funcionalidades CRUD básicas (Create, Read, Update, Delete)
 * através da convenção do Spring Data JPA.
 */
@Repository // Identifica a interface como um repositório gerenciado pelo Spring.
public interface MesaRepository extends JpaRepository<Mesa, Long> {

    // JpaRepository<Mesa, Long>
    // 1. Mesa: Define a Entidade que será gerenciada (a tabela).
    // 2. Long: Define o tipo da Chave Primária (PK) da Entidade Mesa.

    // NOTA: Todos os métodos de CRUD (save, findById, findAll, delete)
    // são fornecidos automaticamente pelo Spring, sem necessidade de implementação manual.
}