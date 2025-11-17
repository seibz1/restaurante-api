package br.com.seib.restaurante.controller;

import br.com.seib.restaurante.model.Mesa;
import br.com.seib.restaurante.service.MesaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller responsável por receber as requisições HTTP e gerenciar as operações
 * de CRUD para a Entidade Mesa.
 */
@RestController // Define a classe como um controlador REST, retornando dados no formato JSON/XML.
@RequestMapping("/api/mesas") // Mapeamento da URI base para todos os métodos (ex: http://localhost:8080/api/mesas)
public class MesaController {

    // Injeta a dependência do Service (a camada de lógica de negócio).
    private final MesaService mesaService;

    @Autowired // Realiza a Injeção de Dependência (DI) via construtor.
    public MesaController(MesaService mesaService) {
        this.mesaService = mesaService;
    }

    /**
     * Cria uma nova mesa no sistema.
     * Mapeia para: POST /api/mesas
     */
    @PostMapping
    public ResponseEntity<Mesa> criarMesa(@RequestBody Mesa mesa) {
        // @RequestBody: Deserializa o JSON do corpo da requisição para um objeto Mesa.
        Mesa novaMesa = mesaService.salvarMesa(mesa);
        // Retorna o status HTTP 201 (Created) e o objeto criado.
        return ResponseEntity.status(HttpStatus.CREATED).body(novaMesa);
    }

    /**
     * Lista todas as mesas cadastradas no sistema.
     * Mapeia para: GET /api/mesas
     */
    @GetMapping
    public ResponseEntity<List<Mesa>> listarMesas() {
        List<Mesa> mesas = mesaService.buscarTodas();
        // Retorna o status HTTP 200 (OK) e a lista de objetos.
        return ResponseEntity.ok(mesas);
    }
}