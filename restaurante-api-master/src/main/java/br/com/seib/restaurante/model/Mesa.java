package br.com.seib.restaurante.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidade que representa a tabela 'mesas', contendo informações essenciais
 * para a lógica de reserva (capacidade e identificação única).
 */
@Data // (Lombok) Gera getters e setters.
@NoArgsConstructor // (Lombok) Construtor vazio.
@Entity(name = "mesas") // Mapeia a classe para a tabela no MySQL.
public class Mesa {

    @Id // Chave Primária (Primary Key).
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Valor gerado por AUTO_INCREMENT do MySQL.
    private Long id;

    /**
     * Identificador da mesa no restaurante.
     * Restrição UNIQUE: Garante que não haja números de mesas duplicados.
     */
    @Column(nullable = false, unique = true)
    private int numeroMesa;

    /**
     * Capacidade máxima de pessoas da mesa.
     * Restrição NOT NULL: Essencial para a validação da regra de negócio (ReservaService).
     */
    @Column(nullable = false)
    private int capacidade;
}