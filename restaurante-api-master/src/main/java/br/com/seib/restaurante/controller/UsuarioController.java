package br.com.seib.restaurante.controller;

import br.com.seib.restaurante.model.Usuario;
import br.com.seib.restaurante.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// @RestController: Combinação de @Controller + @ResponseBody.
// Diz ao Spring que esta classe é um controlador e que os métodos
// devem retornar JSON diretamente no corpo (body) da resposta.
@RestController
// @RequestMapping: Define o URL base para todos os endpoints nesta classe.
// Todas as requisições para "/api/usuarios" cairão aqui.
@RequestMapping("/api/usuarios")
public class UsuarioController {

    // --- Injeção de Dependências ---
    // Pedimos ao Spring o nosso "cérebro" (o Service)
    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // --- Nosso Primeiro Endpoint (Porta de Entrada) ---

    // @PostMapping: Mapeia este método para requisições HTTP POST.
    // Como já estamos em "/api/usuarios", este método responde a:
    // POST http://localhost:8080/api/usuarios
    @PostMapping
    public ResponseEntity<Usuario> cadastrarUsuario(@RequestBody Usuario usuario) {
        // @RequestBody: Pega o JSON enviado no corpo da requisição
        // e o transforma ("desempacota") em um objeto 'Usuario'.

        // 1. Delega o trabalho para o Service
        Usuario novoUsuario = usuarioService.cadastrarUsuario(usuario);

        // 2. Envia a resposta de volta
        // Usamos ResponseEntity para ter controle total sobre a resposta HTTP.
        // HttpStatus.CREATED (201): É o código HTTP correto para "criei algo".
        // .body(novoUsuario): Coloca o usuário salvo (com 'id', etc.) no corpo
        // da resposta como JSON.
        return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);
    }
}