package br.com.seib.restaurante.repository;

import br.com.seib.restaurante.model.ItemCardapio;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Interface Repository para o Documento ItemCardapio.
 * Estende MongoRepository, o que permite o acesso a dados no MongoDB.
 */
@Repository // Identifica a interface como um repositório gerenciado pelo Spring.
public interface ItemCardapioRepository extends MongoRepository<ItemCardapio, String> {

    // MongoRepository<ItemCardapio, String>
    // 1. ItemCardapio: Define o Documento (coleção) gerenciado.
    // 2. String: Define o tipo da Chave Primária (PK) do MongoDB.

    /**
     * Exemplo de Query Derivation: O Spring Data MongoDB gera automaticamente a consulta
     * (query) que busca todos os documentos com base no campo 'categoria'.
     */
    List<ItemCardapio> findByCategoria(String categoria);
}