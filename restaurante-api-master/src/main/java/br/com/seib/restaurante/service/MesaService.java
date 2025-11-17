package br.com.seib.restaurante.service;

import br.com.seib.restaurante.model.Mesa;
import br.com.seib.restaurante.repository.MesaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Camada de Serviço responsável pela aplicação da lógica de negócio e
 * pelo gerenciamento das transações da Entidade Mesa.
 */
@Service // Define a classe como um componente de serviço gerenciado pelo Spring (IoC).
public class MesaService {

    // Injeção de Dependências (IoC): Referência ao agente de dados (Repository).
    private final MesaRepository mesaRepository;

    @Autowired // Realiza a Injeção de Dependência (DI) via construtor.
    public MesaService(MesaRepository mesaRepository) {
        this.mesaRepository = mesaRepository;
    }

    /**
     * Persiste uma nova entidade Mesa no banco de dados.
     * Esta é a camada ideal para validações de unicidade, se necessário.
     */
    public Mesa salvarMesa(Mesa mesa) {
        return mesaRepository.save(mesa);
    }

    /**
     * Retorna a lista de todas as mesas cadastradas.
     * Utiliza o método findAll() herdado do JpaRepository.
     */
    public List<Mesa> buscarTodas() {
        return mesaRepository.findAll();
    }
}