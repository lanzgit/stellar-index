package br.edu.infnet.stellarindexapi.controller;

import br.edu.infnet.stellarindexapi.model.dto.auth.CredenciaisDTO;
import br.edu.infnet.stellarindexapi.model.dto.auth.TokenResponseDTO;
import br.edu.infnet.stellarindexapi.model.dto.auth.UsuarioDTO;
import br.edu.infnet.stellarindexapi.model.service.AutenticacaoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "Autenticação JWT")
public class AutenticacaoController {

  private final AutenticacaoService autenticacaoService;

  @PostMapping("/login")
  public ResponseEntity<TokenResponseDTO> login(@RequestBody CredenciaisDTO credenciais) {
    return ResponseEntity.ok(autenticacaoService.autenticar(credenciais));
  }

  @PostMapping("/registrar")
  public ResponseEntity<String> registrar(@RequestBody UsuarioDTO usuario) {
    autenticacaoService.registrar(usuario);

    return ResponseEntity.ok("Usuário registrado com sucesso");
  }
}
