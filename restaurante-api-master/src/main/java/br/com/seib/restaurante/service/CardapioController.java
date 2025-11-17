package br.com.seib.restaurante.controller;

import br.com.seib.restaurante.model.ItemCardapio;
import br.com.seib.restaurante.service.CardapioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Controller responsável por receber as requisições HTTP e gerenciar as operações
 * do Documento ItemCardapio no banco de dados NoSQL (MongoDB).
 */
@RestController // Define a classe como um controlador REST, retornando JSON.
@RequestMapping("/api/cardapio") // Mapeamento da URI base (http://localhost:8080/api/cardapio)
public class CardapioController {

    private final CardapioService service;

    @Autowired // Realiza a Injeção de Dependência (DI) do Service.
    public CardapioController(CardapioService service) {
        this.service = service;
    }

    /**
     * Adiciona um novo documento à coleção 'cardapio' do MongoDB.
     * Mapeia para: POST /api/cardapio
     */
    @PostMapping
    public ResponseEntity<ItemCardapio> adicionarItem(@RequestBody ItemCardapio item) {
        // @RequestBody: Deserializa o JSON do corpo da requisição para o objeto ItemCardapio.
        ItemCardapio novoItem = service.adicionarItem(item);
        // Retorna o status HTTP 201 (Created) e o objeto criado.
        return ResponseEntity.status(HttpStatus.CREATED).body(novoItem);
    }

    /**
     * Lista todos os itens do cardápio.
     * Mapeia para: GET /api/cardapio
     */
    @GetMapping
    public ResponseEntity<List<ItemCardapio>> buscarTodos() {
        return ResponseEntity.ok(service.buscarTodos());
    }

    /**
     * Busca itens por uma categoria específica. Demonstra o uso de Query Derivation em Mongo.
     * Mapeia para: GET /api/cardapio/categoria/{nomeCategoria}
     */
    @GetMapping("/categoria/{nomeCategoria}")
    public ResponseEntity<List<ItemCardapio>> buscarPorCategoria(
            @PathVariable String nomeCategoria // @PathVariable: Extrai o valor da URI (nomeCategoria).
    ) {
        return ResponseEntity.ok(service.buscarPorCategoria(nomeCategoria));
    }
}