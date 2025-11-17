package br.com.seib.restaurante.service;

import br.com.seib.restaurante.dto.ReservaRequestDTO;
import br.com.seib.restaurante.model.Mesa;
import br.com.seib.restaurante.model.Reserva;
import br.com.seib.restaurante.model.Usuario;
import br.com.seib.restaurante.repository.MesaRepository;
import br.com.seib.restaurante.repository.ReservaRepository;
import br.com.seib.restaurante.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Camada de Serviço responsável pela aplicação das regras de negócio
 * da reserva, gerenciando a transação e as validações.
 */
@Service
public class ReservaService {

    // Regra de Negócio: Duração padrão de 2 horas para uma reserva.
    private static final long DURACAO_RESERVA_HORAS = 2;

    // Injeção de Dependências (IoC): Agentes de acesso ao banco.
    private final ReservaRepository reservaRepository;
    private final UsuarioRepository usuarioRepository;
    private final MesaRepository mesaRepository;

    @Autowired // Construtor para Injeção de Dependência (DI) dos Repositórios.
    public ReservaService(ReservaRepository reservaRepository,
                          UsuarioRepository usuarioRepository,
                          MesaRepository mesaRepository) {
        this.reservaRepository = reservaRepository;
        this.usuarioRepository = usuarioRepository;
        this.mesaRepository = mesaRepository;
    }

    /**
     * Processa a solicitação de reserva vinda do Controller (via DTO),
     * aplica as regras de negócio e persiste a transação.
     */
    public Reserva criarReserva(ReservaRequestDTO dto) {

        // 1. Validação de Integridade: Confere se as FKs (Foreign Keys) existem no banco.
        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));

        Mesa mesa = mesaRepository.findById(dto.getMesaId())
                .orElseThrow(() -> new RuntimeException("Mesa não encontrada!"));

        // 2. Regra de Negócio: Capacidade Máxima. (Requisito de Lógica/Trigger)
        if (dto.getNumeroPessoas() > mesa.getCapacidade()) {
            throw new RuntimeException("Número de pessoas (" + dto.getNumeroPessoas() +
                    ") excede a capacidade da mesa (" + mesa.getCapacidade() + ").");
        }

        // 3. Regra de Negócio: Cálculo de Duração.
        LocalDateTime inicio = dto.getDataHoraInicio();
        LocalDateTime fim = inicio.plusHours(DURACAO_RESERVA_HORAS);

        // 4. Regra de Negócio: Checagem de Conflito de Horário. (Lógica Principal)
        List<Reserva> conflitos = reservaRepository.findReservasConflitantes(
                dto.getMesaId(),
                inicio,
                fim
        );


        if (!conflitos.isEmpty()) {
            // Rejeita a transação se a consulta retornar qualquer conflito.
            throw new RuntimeException("Horário indisponível. Já existe uma reserva para esta mesa neste período.");
        }

        // 5. Persistência: Construção e Salvamento.
        // Constrói o objeto de persistência completo com as entidades (Usuario, Mesa)
        Reserva novaReserva = new Reserva();
        novaReserva.setUsuario(usuario);
        novaReserva.setMesa(mesa);
        novaReserva.setDataHoraInicio(inicio);
        novaReserva.setDataHoraFim(fim);
        novaReserva.setNumeroPessoas(dto.getNumeroPessoas());

        // Retorna o objeto salvo (agora com o ID gerado).
        return reservaRepository.save(novaReserva);
    }
}