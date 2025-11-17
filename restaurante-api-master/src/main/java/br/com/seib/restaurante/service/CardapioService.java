package br.com.seib.restaurante.service;

import br.com.seib.restaurante.model.ItemCardapio;
import br.com.seib.restaurante.repository.ItemCardapioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Camada de Serviço responsável pela lógica de negócio e pelo acesso
 * à coleção 'cardapio' no banco de dados NoSQL (MongoDB).
 */
@Service // Define a classe como um componente de serviço gerenciado pelo Spring (IoC).
public class CardapioService {

    // Injeção de Dependências (IoC): Referência ao agente de dados (Repository Mongo).
    private final ItemCardapioRepository repository;

    @Autowired // Realiza a Injeção de Dependência (DI) via construtor.
    public CardapioService(ItemCardapioRepository repository) {
        this.repository = repository;
    }

    /**
     * Persiste um novo documento ItemCardapio no MongoDB.
     */
    public ItemCardapio adicionarItem(ItemCardapio item) {
        return repository.save(item);
    }

    /**
     * Retorna todos os itens da coleção 'cardapio'.
     */
    public List<ItemCardapio> buscarTodos() {
        return repository.findAll();
    }

    /**
     * Busca itens utilizando a consulta derivada 'findByCategoria', demonstrando
     * a funcionalidade de busca do Spring Data MongoDB.
     */
    public List<ItemCardapio> buscarPorCategoria(String categoria) {
        return repository.findByCategoria(categoria);
    }
}