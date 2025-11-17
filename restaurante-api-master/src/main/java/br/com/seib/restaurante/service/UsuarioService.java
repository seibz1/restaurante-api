package br.com.seib.restaurante.service;

import br.com.seib.restaurante.model.GrupoUsuario;
import br.com.seib.restaurante.model.Usuario;
import br.com.seib.restaurante.repository.GrupoUsuarioRepository;
import br.com.seib.restaurante.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Camada de Serviço responsável pela aplicação da lógica de negócio e
 * pelo gerenciamento de usuários, incluindo validação e atribuição de grupo padrão.
 */
@Service // Define a classe como um componente de serviço gerenciado pelo Spring (IoC).
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final GrupoUsuarioRepository grupoUsuarioRepository;

    // Construtor para Injeção de Dependência (DI) dos Repositórios.
    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, GrupoUsuarioRepository grupoUsuarioRepository) {
        this.usuarioRepository = usuarioRepository;
        this.grupoUsuarioRepository = grupoUsuarioRepository;
    }

    /**
     * Processa o cadastro de um novo usuário, aplicando as regras de validação e atribuição de grupo.
     */
    public Usuario cadastrarUsuario(Usuario usuario) {

        // 1. REGRA DE NEGÓCIO: Verificar unicidade do e-mail.
        Optional<Usuario> emailExistente = usuarioRepository.findByEmail(usuario.getEmail());

        if (emailExistente.isPresent()) {
            // Lança uma exceção se o e-mail já estiver em uso.
            throw new RuntimeException("Email já cadastrado.");
        }

        // 2. LÓGICA DE INICIALIZAÇÃO: Encontra ou cria o grupo padrão "CLIENTE".
        GrupoUsuario grupoCliente = grupoUsuarioRepository.findByNome("CLIENTE");

        if (grupoCliente == null) {
            // Se for o primeiro usuário, cria o grupo "CLIENTE" no banco de dados.
            grupoCliente = new GrupoUsuario();
            grupoCliente.setNome("CLIENTE");
            grupoUsuarioRepository.save(grupoCliente);
        }

        // 3. ATRIBUIÇÃO: Associa o usuário ao grupo padrão.
        usuario.setGrupo(grupoCliente);

        // TODO: REQUISITO DE SEGURANÇA - Criptografar a senha (usando Spring Security/PasswordEncoder)
        // O código final deve incluir criptografia antes da persistência.

        // 4. PERSISTÊNCIA: Salva o usuário no banco e o retorna.
        return usuarioRepository.save(usuario);
    }
}