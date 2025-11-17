package br.com.seib.restaurante.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;
import java.util.List;

/**
 * Representa um Documento na coleção 'cardapio' do MongoDB.
 * A estrutura flexível do NoSQL é ideal para dados complexos como um menu.
 */
@Data // (Lombok) Gera getters e setters.
@Document(collection = "cardapio") // Mapeia a classe para a coleção "cardapio" no MongoDB.
public class ItemCardapio {

    // @Id: Define a Chave Primária, que é gerenciada pelo MongoDB e é do tipo String.
    @Id
    private String id;

    private String nome;
    private String descricao;
    private String categoria; // Utilizado para consultas de listagem (ex: 'Bebida', 'Lanche').

    // BigDecimal é usado para garantir a precisão de valores monetários.
    private BigDecimal preco;

    /**
     * Demonstra a flexibilidade do modelo de documento:
     * Campos podem ser arrays ou listas de dados sem precisar de tabelas de junção.
     */
    private List<String> ingredientes;
}