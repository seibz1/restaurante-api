package br.com.seib.restaurante.repository;

import br.com.seib.restaurante.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Interface Repository para a Entidade Reserva.
 * Contém a lógica personalizada para detecção de conflito de horários.
 */
@Repository // Identifica a interface como um repositório gerenciado pelo Spring.
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    /**
     * Consulta personalizada para detectar qualquer sobreposição de horários
     * para uma mesa específica, baseada no princípio matemático de conflito de intervalos:
     * (Inicio Antigo < Fim Novo) E (Fim Antigo > Inicio Novo).
     */
    @Query("SELECT r FROM reservas r " + // JPQL (linguagem do JPA)
            "WHERE r.mesa.id = :mesaId " +
            "AND r.dataHoraInicio < :fimNovoPeriodo " + // A reserva antiga começa ANTES da nova terminar
            "AND r.dataHoraFim > :inicioNovoPeriodo")   // E a reserva antiga termina DEPOIS da nova começar
    List<Reserva> findReservasConflitantes(
            @Param("mesaId") Long mesaId, // @Param liga a variável Java ao nome da variável JPQL (:mesaId)
            @Param("inicioNovoPeriodo") LocalDateTime inicioNovoPeriodo,
            @Param("fimNovoPeriodo") LocalDateTime fimNovoPeriodo
    );


    /**
     * Exemplo de Query Derivation: Spring gera a consulta SQL automaticamente
     * a partir do nome do método.
     * SQL: SELECT * FROM reservas WHERE mesa_id = ? ORDER BY data_hora_inicio ASC
     */
    List<Reserva> findByMesaIdOrderByDataHoraInicioAsc(Long mesaId);
}