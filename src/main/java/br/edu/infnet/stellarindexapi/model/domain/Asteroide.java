package br.edu.infnet.stellarindexapi.model.domain;

import br.edu.infnet.stellarindexapi.model.clients.NasaSbdbClient;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Asteroide extends Astro {

    @NotNull(message = "A designação é obrigatória")
    private String designacao;
    
    private String nomeCompleto;
    private String classificacaoOrbital;
    private String periodoOrbital;
    private String tipo;
    private boolean neo;
    private boolean pha;

    public void copyFromCorpoCelesteResponse(CorpoCeleste corpoCeleste) {
        this.nomeCompleto = corpoCeleste.getNomeCompleto();
        this.classificacaoOrbital = corpoCeleste.getClassificacaoOrbital();
        this.periodoOrbital = corpoCeleste.getPeriodoOrbital();
        this.tipo = corpoCeleste.getTipo();
        this.neo = corpoCeleste.ehNEO();
        this.pha = corpoCeleste.ehPHA();
    }

    @Override
    public String obterTipo() {
        return "Asteroide";
    }
}
