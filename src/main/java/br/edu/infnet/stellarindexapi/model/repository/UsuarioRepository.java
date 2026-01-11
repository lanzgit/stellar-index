package br.edu.infnet.stellarindexapi.model.repository;

import br.edu.infnet.stellarindexapi.model.domain.Usuario;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
  Optional<Usuario> findByUsername(String username);
}
