package br.edu.infnet.stellarindexapi.model.clients;

import lombok.Getter;
import lombok.Setter;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "viniciusviannaapi", url="${viniciusviannaapi.url}")
public interface CorpoCelesteClient {

    @GetMapping("/corpo-celeste/{designacao}")
    CorpoCelesteResponse obterCorpoCelestePorDesignacao(@PathVariable String designacao);

    @GetMapping("/corpo-celeste/{designacao}/detalhado")
    CorpoCelesteResponse obterCorpoCelesteDetalhado(@PathVariable String designacao);

    @GetMapping("/neos")
    List<CorpoCelesteResponse> obterNeos();

    @Getter
    @Setter
    class CorpoCelesteResponse {
        private String nomeCompleto;
        private String classificacaoOrbital;
        private String periodoOrbital;
        private String tipo;
        private boolean neo;
        private boolean pha;
        private String codigoClassificacao;
        private String distanciaMinima;

        public boolean ehPotencialmentePerigoso() {
            return this.pha && this.neo;
        }
    }
}
