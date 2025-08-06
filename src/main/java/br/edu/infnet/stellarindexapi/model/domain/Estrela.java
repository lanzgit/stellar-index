package br.edu.infnet.stellarindexapi.model.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Estrela extends Astro {
    private Integer estrelaId;
    private String constelacao;

    @Override
    public String obterTipo() {
        return "Estrla";
    }
}
