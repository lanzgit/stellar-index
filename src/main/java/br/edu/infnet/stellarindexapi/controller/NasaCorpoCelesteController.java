package br.edu.infnet.stellarindexapi.controller;

import br.edu.infnet.stellarindexapi.model.dto.CorpoCelesteDTO;
import br.edu.infnet.stellarindexapi.model.service.NasaCorpoCelesteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/nasa/corpos-celeste")
@RequiredArgsConstructor
@Tag(name = "CorposCeleste", description = "Gerenciador de Corpos Celestes da NASA")
public class NasaCorpoCelesteController {

    private final NasaCorpoCelesteService nasaCorpoCelesteService;

    @GetMapping("/{designacao}")
    public ResponseEntity<CorpoCelesteDTO> buscar(@PathVariable String designacao) {
        return ResponseEntity.ok(nasaCorpoCelesteService.buscarCorpoCeleste(designacao));
    }

    @GetMapping("/neos")
    public ResponseEntity<List<CorpoCelesteDTO>> listarNeos() {
        return ResponseEntity.ok(nasaCorpoCelesteService.buscarNeosSimplificados());
    }
}
