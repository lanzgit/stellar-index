package br.edu.infnet.stellarindexapi.controller;

import br.edu.infnet.stellarindexapi.model.domain.Planeta;
import br.edu.infnet.stellarindexapi.model.service.PlanetaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;


@RestController
@RequestMapping("/api")
@Tag(name = "Planetas", description = "Gerenciado de Planetas")
public class PlanetaController {

    private final PlanetaService planetaService;

    public PlanetaController(PlanetaService planetaService) {
        this.planetaService = planetaService;
    }

    @GetMapping("/planeta/{id}")
    public ResponseEntity<Planeta> obterPorId(@PathVariable Integer id) {
        Planeta planeta = this.planetaService.obterPorId(id);
        return ResponseEntity.status(OK).body(planeta);
    }

    @GetMapping("/planetas")
    public ResponseEntity<List<Planeta>> obterPlanetas() {
        List<Planeta> planetas = this.planetaService.obterTodos();
        return ResponseEntity.status(OK).body(planetas);
    }

    @PostMapping("/planeta")
    public ResponseEntity<Planeta> criarPlaneta(@RequestBody Planeta planeta) {
        Planeta novoPlaneta = this.planetaService.salvar(planeta);
        return ResponseEntity.status(CREATED).body(novoPlaneta);
    }

    @PutMapping("/planeta/{id}")
    public ResponseEntity<Planeta> atualizarPlaneta(@PathVariable Integer id, @RequestBody Planeta planeta) {
        Planeta planetaAtualizado = this.planetaService.atualizar(planeta, id);

        return ResponseEntity.status(OK).body(planetaAtualizado);
    }

    @DeleteMapping("/planeta/{id}")
    public ResponseEntity<Void> excluirPlaneta(@PathVariable Integer id) {
        this.planetaService.excluir(id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/planeta/{id}/nuke")
    public ResponseEntity<Planeta> destruir(@PathVariable Integer id) {
        Planeta planetaASerDestruido = this.planetaService.nuke(id);

        return ResponseEntity.status(OK).body(planetaASerDestruido);
    }
}
