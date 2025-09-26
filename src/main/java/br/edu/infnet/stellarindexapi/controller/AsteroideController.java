package br.edu.infnet.stellarindexapi.controller;

import br.edu.infnet.stellarindexapi.model.domain.Asteroide;
import br.edu.infnet.stellarindexapi.model.service.AsteroideService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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

import java.util.List;

@RestController
@RequestMapping("api")
@Tag(name = "Asteroides", description = "Gerenciador de Asteroides")
@RequiredArgsConstructor
public class AsteroideController {

    private final AsteroideService asteroideService;

    @GetMapping("/asteroides")
    public ResponseEntity<List<Asteroide>> obterTodosOsAsteroides() {
        List<Asteroide> asteroides = this.asteroideService.obterTodos();
        if (asteroides.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(asteroides);
    }

    @GetMapping("/asteroide/{id}")
    public ResponseEntity<Asteroide> obterAsteroidePorId(@PathVariable Integer id) {
        Asteroide asteroide = this.asteroideService.obterPorId(id);
        return ResponseEntity.ok(asteroide);
    }

    @PostMapping("/asteroide")
    public ResponseEntity<Asteroide> criarAsteroide(@Valid @RequestBody Asteroide asteroide) {
        Asteroide novoAsteroide = this.asteroideService.criar(asteroide);
        return ResponseEntity.status(201).body(novoAsteroide);
    }

    @PutMapping("/asteroide/{id}")
    public ResponseEntity<Asteroide> atualizarAsteroide(@PathVariable Integer id, @RequestBody Asteroide asteroide) {
        Asteroide asteroideAtualizado = this.asteroideService.atualizar(asteroide, id);
        return ResponseEntity.ok(asteroideAtualizado);
    }

    @DeleteMapping("/asteroide/{id}")
    public ResponseEntity<Void> excluirAsteroide(@PathVariable Integer id) {
        this.asteroideService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
