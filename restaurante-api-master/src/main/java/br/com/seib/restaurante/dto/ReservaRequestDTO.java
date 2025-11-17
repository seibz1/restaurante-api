package br.com.seib.restaurante.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * DTO (Data Transfer Object) utilizado para encapsular e transferir
 * os dados de entrada de uma solicitação de reserva da camada Controller para a Service.
 * * Justificativa: Garante a separação de responsabilidades (separation of concerns),
 * pois o frontend só precisa enviar IDs e dados brutos, sem manipular a Entidade Reserva completa.
 */
@Data // (Lombok) Gera getters, setters, equals, e hashCode.
public class ReservaRequestDTO {

    // Chave Estrangeira (FK) do usuário que está fazendo a reserva.
    private Long usuarioId;

    // Chave Estrangeira (FK) da mesa que será reservada.
    private Long mesaId;

    // Horário de início solicitado para a reserva.
    private LocalDateTime dataHoraInicio;

    // Número de pessoas (usado para checagem de capacidade).
    private int numeroPessoas;

}