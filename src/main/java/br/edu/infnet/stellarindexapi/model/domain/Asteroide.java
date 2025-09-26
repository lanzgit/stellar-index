package br.edu.infnet.stellarindexapi.model.domain;

import br.edu.infnet.stellarindexapi.model.clients.CorpoCelesteClient;
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

    public void copyFromCorpoCelesteResponse(CorpoCelesteClient.CorpoCelesteResponse response) {
        this.nomeCompleto = response.getNomeCompleto();
        this.classificacaoOrbital = response.getClassificacaoOrbital();
        this.periodoOrbital = response.getPeriodoOrbital();
        this.tipo = response.getTipo();
        this.neo = response.isNeo();
        this.pha = response.ehPotencialmentePerigoso();
    }

    @Override
    public String obterTipo() {
        return "Asteroide";
    }
}
