package br.edu.infnet.stellarindexapi.controller;

import br.edu.infnet.stellarindexapi.model.domain.Asteroide;
import br.edu.infnet.stellarindexapi.model.dto.AsteroideDTO;
import br.edu.infnet.stellarindexapi.model.dto.AsteroideMapper;
import br.edu.infnet.stellarindexapi.model.service.AsteroideService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
@Tag(name = "Asteroides", description = "Gerenciador de Asteroides")
@RequiredArgsConstructor
public class AsteroideController {

  private final AsteroideService asteroideService;

  @GetMapping("/asteroides")
  public ResponseEntity<List<AsteroideDTO>> obterTodosOsAsteroides() {
    List<Asteroide> asteroides = this.asteroideService.obterTodos();
    if (asteroides.isEmpty()) {
      return ResponseEntity.noContent().build();
    }
    List<AsteroideDTO> asteroidesDTO =
        asteroides.stream().map(AsteroideMapper::toDTO).collect(Collectors.toList());
    return ResponseEntity.ok(asteroidesDTO);
  }

  @GetMapping("/asteroide/{id}")
  public ResponseEntity<AsteroideDTO> obterAsteroidePorId(@PathVariable Integer id) {
    Asteroide asteroide = this.asteroideService.obterPorId(id);
    return ResponseEntity.ok(AsteroideMapper.toDTO(asteroide));
  }

  @PostMapping("/asteroide")
  public ResponseEntity<AsteroideDTO> criarAsteroide(
      @Valid @RequestBody AsteroideDTO asteroideDTO) {
    Asteroide asteroide = AsteroideMapper.toEntity(asteroideDTO);
    Asteroide novoAsteroide = this.asteroideService.criar(asteroide);
    return ResponseEntity.status(201).body(AsteroideMapper.toDTO(novoAsteroide));
  }

  @PutMapping("/asteroide/{id}")
  public ResponseEntity<AsteroideDTO> atualizarAsteroide(
      @PathVariable Integer id, @Valid @RequestBody AsteroideDTO asteroideDTO) {
    Asteroide asteroide = AsteroideMapper.toEntity(asteroideDTO);
    Asteroide asteroideAtualizado = this.asteroideService.atualizar(asteroide, id);
    return ResponseEntity.ok(AsteroideMapper.toDTO(asteroideAtualizado));
  }

  @DeleteMapping("/asteroide/{id}")
  public ResponseEntity<Void> excluirAsteroide(@PathVariable Integer id) {
    this.asteroideService.excluir(id);
    return ResponseEntity.noContent().build();
  }
}
