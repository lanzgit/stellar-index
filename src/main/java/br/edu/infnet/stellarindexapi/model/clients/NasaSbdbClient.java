package br.edu.infnet.stellarindexapi.model.clients;

import br.edu.infnet.stellarindexapi.config.NasaFeignConfig;
import br.edu.infnet.stellarindexapi.model.domain.CorpoCeleste;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
    name = "nasa-sbdb-client", 
    url = "${api.nasa.base-url}",
    configuration = NasaFeignConfig.class
)
public interface NasaSbdbClient {
    
    @GetMapping("/sbdb.api")
    CorpoCeleste buscarCorpoCeleste(
        @RequestParam("sstr") String designation
    );
    
    @GetMapping("/sbdb.api")
    CorpoCeleste buscarCorpoCelesteDetalhado(
        @RequestParam("sstr") String designation,
        @RequestParam("full-prec") boolean fullPrecision
    );
}
