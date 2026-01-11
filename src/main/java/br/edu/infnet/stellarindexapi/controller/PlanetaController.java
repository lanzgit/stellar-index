package br.edu.infnet.stellarindexapi.controller;

import static org.springframework.http.HttpStatus.*;

import br.edu.infnet.stellarindexapi.model.domain.Planeta;
import br.edu.infnet.stellarindexapi.model.dto.PlanetaDTO;
import br.edu.infnet.stellarindexapi.model.dto.PlanetaMapper;
import br.edu.infnet.stellarindexapi.model.service.PlanetaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Tag(name = "Planetas", description = "Gerenciador de Planetas")
@RequiredArgsConstructor
public class PlanetaController {

  private final PlanetaService planetaService;
  private final PlanetaMapper planetaMapper;

  @GetMapping("/planeta/{id}")
  public ResponseEntity<PlanetaDTO> obterPorId(@PathVariable Integer id) {
    Planeta planeta = this.planetaService.obterPorId(id);
    return ResponseEntity.status(OK).body(planetaMapper.toDTO(planeta));
  }

  @GetMapping("/planetas")
  public ResponseEntity<List<PlanetaDTO>> obterPlanetas() {
    List<Planeta> planetas = this.planetaService.obterTodos();
    if (planetas.isEmpty()) {
      return ResponseEntity.noContent().build();
    }
    List<PlanetaDTO> planetasDTO =
        planetas.stream().map(planetaMapper::toDTO).collect(Collectors.toList());
    return ResponseEntity.status(OK).body(planetasDTO);
  }

  @PostMapping("/planeta")
  @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
  public ResponseEntity<PlanetaDTO> criarPlaneta(@Valid @RequestBody PlanetaDTO planetaDTO) {
    Planeta planeta = planetaMapper.toEntity(planetaDTO);
    Planeta novoPlaneta = this.planetaService.criar(planeta);
    return ResponseEntity.status(CREATED).body(planetaMapper.toDTO(novoPlaneta));
  }

  @PutMapping("/planeta/{id}")
  @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
  public ResponseEntity<PlanetaDTO> atualizarPlaneta(
      @PathVariable Integer id, @Valid @RequestBody PlanetaDTO planetaDTO) {
    Planeta planeta = planetaMapper.toEntity(planetaDTO);
    Planeta planetaAtualizado = this.planetaService.atualizar(planeta, id);
    return ResponseEntity.status(OK).body(planetaMapper.toDTO(planetaAtualizado));
  }

  @DeleteMapping("/planeta/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Void> excluirPlaneta(@PathVariable Integer id) {
    this.planetaService.excluir(id);
    return ResponseEntity.noContent().build();
  }

  @PatchMapping("/planeta/{id}/nuke")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<PlanetaDTO> destruir(@PathVariable Integer id) {
    Planeta planetaASerDestruido = this.planetaService.nuke(id);
    return ResponseEntity.status(OK).body(planetaMapper.toDTO(planetaASerDestruido));
  }
}
