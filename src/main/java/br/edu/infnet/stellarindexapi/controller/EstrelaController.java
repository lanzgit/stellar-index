package br.edu.infnet.stellarindexapi.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.infnet.stellarindexapi.model.domain.Estrela;
import br.edu.infnet.stellarindexapi.model.service.EstrelaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api")
@Tag(name = "Estrelas", description = "Gerenciador de Estrelas")
public class EstrelaController {

    private final EstrelaService estrelaService;

    public EstrelaController(EstrelaService estrelaService) {
        this.estrelaService = estrelaService;
    }

    @GetMapping("/estrelas")
    public ResponseEntity<List<Estrela>> obterEstrelas() {
        List<Estrela> estrelas = this.estrelaService.obterTodos();
        if (estrelas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(OK).body(estrelas);
    }

    @GetMapping("/estrela/{id}")
    public ResponseEntity<Estrela> obterPorId(@PathVariable Integer id) {
        Estrela estrela = this.estrelaService.obterPorId(id);
        return ResponseEntity.status(OK).body(estrela);
    }

    @PostMapping("/estrela")
    public ResponseEntity<Estrela> criarEstrela(@Valid @RequestBody Estrela estrela) {
        Estrela novaEstrela = this.estrelaService.criar(estrela);
        return ResponseEntity.status(CREATED).body(novaEstrela);
    }

    @DeleteMapping("/estrela/{id}")
    public ResponseEntity<Void> deletarEstrela(@PathVariable Integer id) {
        this.estrelaService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/estrela/{id}")
    public ResponseEntity<Estrela> atualizarEstrela(@PathVariable Integer id, @RequestBody Estrela estrela) {
        Estrela estrelaAtualizada = this.estrelaService.atualizar(estrela, id);
        return ResponseEntity.status(OK).body(estrelaAtualizada);
    }
}
