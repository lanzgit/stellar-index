package br.edu.infnet.stellarindexapi.controller;

import br.edu.infnet.stellarindexapi.model.domain.Planeta;
import br.edu.infnet.stellarindexapi.model.dto.PlanetaDTO;
import br.edu.infnet.stellarindexapi.model.dto.PlanetaMapper;
import br.edu.infnet.stellarindexapi.model.service.PlanetaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;


@RestController
@RequestMapping("/api")
@Tag(name = "Planetas", description = "Gerenciador de Planetas")
public class PlanetaController {

    private final PlanetaService planetaService;

    public PlanetaController(PlanetaService planetaService) {
        this.planetaService = planetaService;
    }

    @GetMapping("/planeta/{id}")
    public ResponseEntity<PlanetaDTO> obterPorId(@PathVariable Integer id) {
        Planeta planeta = this.planetaService.obterPorId(id);
        return ResponseEntity.status(OK).body(PlanetaMapper.toDTO(planeta));
    }

    @GetMapping("/planetas")
    public ResponseEntity<List<PlanetaDTO>> obterPlanetas() {
        List<Planeta> planetas = this.planetaService.obterTodos();
        if (planetas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<PlanetaDTO> planetasDTO = planetas.stream()
                .map(PlanetaMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.status(OK).body(planetasDTO);
    }

    @PostMapping("/planeta")
    public ResponseEntity<PlanetaDTO> criarPlaneta(@Valid @RequestBody PlanetaDTO planetaDTO) {
        Planeta planeta = PlanetaMapper.toEntity(planetaDTO);
        Planeta novoPlaneta = this.planetaService.criar(planeta);
        return ResponseEntity.status(CREATED).body(PlanetaMapper.toDTO(novoPlaneta));
    }

    @PutMapping("/planeta/{id}")
    public ResponseEntity<PlanetaDTO> atualizarPlaneta(@PathVariable Integer id, @Valid @RequestBody PlanetaDTO planetaDTO) {
        Planeta planeta = PlanetaMapper.toEntity(planetaDTO);
        Planeta planetaAtualizado = this.planetaService.atualizar(planeta, id);
        return ResponseEntity.status(OK).body(PlanetaMapper.toDTO(planetaAtualizado));
    }

    @DeleteMapping("/planeta/{id}")
    public ResponseEntity<Void> excluirPlaneta(@PathVariable Integer id) {
        this.planetaService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/planeta/{id}/nuke")
    public ResponseEntity<PlanetaDTO> destruir(@PathVariable Integer id) {
        Planeta planetaASerDestruido = this.planetaService.nuke(id);
        return ResponseEntity.status(OK).body(PlanetaMapper.toDTO(planetaASerDestruido));
    }
}
