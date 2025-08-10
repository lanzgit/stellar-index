package br.edu.infnet.stellarindexapi.model.domain;

import br.edu.infnet.stellarindexapi.model.enums.LuminosidadeEnum;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Estrela extends Astro {

    private String constelacao;
    private LuminosidadeEnum luminosidade;

    @Override
    public String obterTipo() {
        return "Estrela";
    }

    @Override
    public String toString() {
        return String.format("%s | %s - %s", constelacao, luminosidade.getNome(), luminosidade.getClasse());
    }
}
