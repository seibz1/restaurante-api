package br.com.seib.restaurante.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Entidade central do sistema, representando uma transação de reserva.
 * Contém duas Chaves Estrangeiras, ligando o Usuário e a Mesa.
 */
@Data // (Lombok) Gera getters e setters.
@NoArgsConstructor // (Lombok) Construtor vazio.
@Entity(name = "reservas") // Mapeia a classe para a tabela 'reservas'.
public class Reserva {

    @Id // Chave Primária.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Valor gerado por AUTO_INCREMENT.
    private Long id;

    /**
     * RELACIONAMENTO N:1 (Chave Estrangeira 1): O usuário que fez a reserva.
     * Coluna FK gerada: 'usuario_id' (NOT NULL).
     */
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    /**
     * RELACIONAMENTO N:1 (Chave Estrangeira 2): A mesa reservada.
     * Coluna FK gerada: 'mesa_id' (NOT NULL).
     */
    @ManyToOne
    @JoinColumn(name = "mesa_id", nullable = false)
    private Mesa mesa;


    /**
     * Horário de início da reserva.
     * Tipo LocalDateTime utilizado para precisão temporal.
     */
    @Column(nullable = false, name = "data_hora_inicio")
    private LocalDateTime dataHoraInicio;

    /**
     * Horário de fim da reserva.
     * Essencial para a lógica de conflito de horários no ReservaService.
     */
    @Column(nullable = false, name = "data_hora_fim")
    private LocalDateTime dataHoraFim;

    /**
     * Número de pessoas envolvidas na reserva.
     * Utilizado para validar a capacidade da Mesa (Regra de Negócio).
     */
    @Column(nullable = false)
    private int numeroPessoas;
}