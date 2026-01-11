package br.edu.infnet.stellarindexapi.model.service;

import br.edu.infnet.stellarindexapi.model.domain.Usuario;
import br.edu.infnet.stellarindexapi.model.dto.auth.CredenciaisDTO;
import br.edu.infnet.stellarindexapi.model.dto.auth.TokenResponseDTO;
import br.edu.infnet.stellarindexapi.model.dto.auth.UsuarioDTO;
import br.edu.infnet.stellarindexapi.model.repository.UsuarioRepository;
import br.edu.infnet.stellarindexapi.security.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AutenticacaoService {

  private final UsuarioRepository usuarioRepository;
  private final TokenService tokenService;
  private final PasswordEncoder passwordEncoder;

  public TokenResponseDTO autenticar(CredenciaisDTO credenciais) {
    Usuario usuario =
        usuarioRepository
            .findByUsername(credenciais.username())
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

    if (!passwordEncoder.matches(credenciais.senha(), usuario.getSenha())) {
      throw new RuntimeException("Senha inválida");
    }

    String token = tokenService.criarToken(usuario.getUsername(), usuario.getPapel());

    return new TokenResponseDTO(token, "Bearer", usuario.getUsername(), usuario.getPapel());
  }

  public void registrar(UsuarioDTO usuario) {
    if (usuarioRepository.findByUsername(usuario.username()).isPresent()) {
      throw new RuntimeException("Nome de usuário já existe");
    }

    Usuario novoUsuario = new Usuario();
    novoUsuario.setUsername(usuario.username());
    novoUsuario.setSenha(passwordEncoder.encode(usuario.senha()));
    novoUsuario.setPapel("ROLE_" + usuario.papel().toUpperCase());

    usuarioRepository.save(novoUsuario);
  }
}
