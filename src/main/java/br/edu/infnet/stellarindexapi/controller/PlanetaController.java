package br.edu.infnet.stellarindexapi.controller;

import br.edu.infnet.stellarindexapi.model.domain.Planeta;
import br.edu.infnet.stellarindexapi.model.service.PlanetaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;
import java.util.List;

@RestController
@RequestMapping("/api/planetas")
@Tag(name = "Planetas", description = "Gerenciado de Planetas")
public class PlanetaController {

    private final PlanetaService planetaService;

    public PlanetaController(PlanetaService planetaService) {
        this.planetaService = planetaService;
    }

    @GetMapping("/{id}")
    public Planeta obterPlaneta(@PathVariable Integer id) {
        return this.planetaService.obter(id);
    }

    @GetMapping
    public List<Planeta> obterPlanetas() {
        return this.planetaService.obterTodos();
    }

    @PostMapping
    public ResponseEntity<Planeta> criarPlaneta(@RequestBody Planeta planeta) {
        Planeta novoPlaneta = this.planetaService.salvar(planeta);

        return ResponseEntity.status(HttpStatus.CREATED).body(novoPlaneta);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Planeta> atualizarPlaneta(@PathVariable Integer id, @RequestBody Planeta planeta) {
        Planeta planetaAtualizado = this.planetaService.atualizar(planeta);

        return ResponseEntity.ok().body(planetaAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirPlaneta(@PathVariable Integer id) {
        this.planetaService.excluir(id);

        return ResponseEntity.noContent().build();
    }
}
