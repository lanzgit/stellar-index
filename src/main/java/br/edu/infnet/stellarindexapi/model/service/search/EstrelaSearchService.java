package br.edu.infnet.stellarindexapi.model.service.search;

import br.edu.infnet.stellarindexapi.model.dto.EstrelaDoc;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EstrelaSearchService {

  private static final String INDEX = "estrelas";
  private final ElasticsearchClient elasticsearchClient;

  public List<EstrelaDoc> buscarPorDescricao(String texto, int page, int size) throws IOException {

    SearchResponse<EstrelaDoc> response =
        elasticsearchClient.search(
            s ->
                s.index(INDEX)
                    .from(page * size)
                    .size(size)
                    .query(
                        q ->
                            q.match(
                                m ->
                                    m.field("descricao")
                                        .query(texto)
                                        .boost(2.0f)
                                        .fuzziness("AUTO")))
                    .highlight(h -> h.fields("descricao", f -> f.preTags("<em>").postTags("</em>")))
                    .sort(so -> so.score(sc -> sc.order(SortOrder.Desc))),
            EstrelaDoc.class);

    List<EstrelaDoc> docs = new ArrayList<>();

    for (Hit<EstrelaDoc> hit : response.hits().hits()) {
      EstrelaDoc doc = hit.source();
      if (doc == null) {
        continue;
      }

      doc.setScore(hit.score() != null ? hit.score() : 0.0);
      docs.add(doc);
    }

    return docs;
  }
}
