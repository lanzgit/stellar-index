package br.edu.infnet.stellarindexapi.controller.search;

import br.edu.infnet.stellarindexapi.model.dto.EstrelaDoc;
import br.edu.infnet.stellarindexapi.model.service.search.EstrelaSearchService;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/estrelas/search")
@Tag(name = "Busca Estrelas", description = "Busca Estrelas no ElasticSearch")
@CrossOrigin(origins = "*")
public class EstrelaSearchController {

  private final EstrelaSearchService estrelaSearchService;
  private final ElasticsearchClient elasticsearchClient;

  @GetMapping("/health")
  public ResponseEntity<String> healthCheck() {
    try {
      var response = elasticsearchClient.cluster().health();
      return ResponseEntity.ok("Elasticsearch conectado! Status: " + response.status());
    } catch (IOException e) {
      return ResponseEntity.status(500).body("Erro ao conectar: " + e.getMessage());
    }
  }

  @GetMapping
  public ResponseEntity<List<EstrelaDoc>> buscarPorTexto(
      @RequestParam String texto, @RequestParam int page, @RequestParam int size)
      throws IOException {
    List<EstrelaDoc> resultados = estrelaSearchService.buscarPorDescricao(texto, page, size);
    if (resultados.isEmpty()) {
      return ResponseEntity.noContent().build();
    }

    return ResponseEntity.ok(resultados);
  }
}
