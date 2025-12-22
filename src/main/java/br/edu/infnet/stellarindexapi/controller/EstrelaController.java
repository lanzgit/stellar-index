package br.edu.infnet.stellarindexapi.controller;

import java.util.List;
import java.util.stream.Collectors;

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
import br.edu.infnet.stellarindexapi.model.dto.EstrelaDTO;
import br.edu.infnet.stellarindexapi.model.dto.EstrelaMapper;
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
    public ResponseEntity<List<EstrelaDTO>> obterEstrelas() {
        List<Estrela> estrelas = this.estrelaService.obterTodos();
        if (estrelas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<EstrelaDTO> estrelasDTO = estrelas.stream()
                .map(EstrelaMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.status(OK).body(estrelasDTO);
    }

    @GetMapping("/estrela/{id}")
    public ResponseEntity<EstrelaDTO> obterPorId(@PathVariable Integer id) {
        Estrela estrela = this.estrelaService.obterPorId(id);
        return ResponseEntity.status(OK).body(EstrelaMapper.toDTO(estrela));
    }

    @PostMapping("/estrela")
    public ResponseEntity<EstrelaDTO> criarEstrela(@Valid @RequestBody EstrelaDTO estrelaDTO) {
        Estrela estrela = EstrelaMapper.toEntity(estrelaDTO);
        Estrela novaEstrela = this.estrelaService.criar(estrela);
        return ResponseEntity.status(CREATED).body(EstrelaMapper.toDTO(novaEstrela));
    }

    @DeleteMapping("/estrela/{id}")
    public ResponseEntity<Void> deletarEstrela(@PathVariable Integer id) {
        this.estrelaService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/estrela/{id}")
    public ResponseEntity<EstrelaDTO> atualizarEstrela(@PathVariable Integer id, @Valid @RequestBody EstrelaDTO estrelaDTO) {
        Estrela estrela = EstrelaMapper.toEntity(estrelaDTO);
        Estrela estrelaAtualizada = this.estrelaService.atualizar(estrela, id);
        return ResponseEntity.status(OK).body(EstrelaMapper.toDTO(estrelaAtualizada));
    }
}
