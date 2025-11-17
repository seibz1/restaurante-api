package br.com.seib.restaurante.controller;

import br.com.seib.restaurante.dto.ReservaRequestDTO;
import br.com.seib.restaurante.model.Reserva;
import br.com.seib.restaurante.service.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller responsável por receber as requisições HTTP e gerenciar a criação de reservas.
 * Esta é a porta de entrada para a lógica de negócio do sistema.
 */
@RestController // Define a classe como um controlador REST, retornando dados diretamente no corpo (JSON).
@RequestMapping("/api/reservas") // Mapeia a URI base: http://localhost:8080/api/reservas
public class ReservaController {

    // Injeta a dependência do Service (a camada de lógica).
    private final ReservaService reservaService;

    @Autowired // Realiza a Injeção de Dependência (DI) do Service.
    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    /**
     * Cria uma nova reserva, delegando as validações para a camada Service.
     * Mapeia para: POST /api/reservas
     */
    @PostMapping
    public ResponseEntity<Reserva> criarReserva(@RequestBody ReservaRequestDTO dto) {
        // @RequestBody: Deserializa o JSON do corpo da requisição para o Objeto de Transferência (DTO).

        // 1. Delega a lógica de validação de conflito e persistência ao Service.
        Reserva novaReserva = reservaService.criarReserva(dto);

        // 2. Retorna a resposta HTTP.
        // ResponseEntity: Permite controlar o código de status.
        // HttpStatus.CREATED (201): Código padrão para indicar que um novo recurso foi criado com sucesso.
        return ResponseEntity.status(HttpStatus.CREATED).body(novaReserva);
    }
}