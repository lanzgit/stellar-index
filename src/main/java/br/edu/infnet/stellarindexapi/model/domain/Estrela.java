package br.edu.infnet.stellarindexapi.model.domain;

import br.edu.infnet.stellarindexapi.model.enums.ConstelacaoEnum;
import br.edu.infnet.stellarindexapi.model.enums.LuminosidadeEnum;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Estrela extends Astro {

    //TODO: transformar em objeto no futuro
    @NotNull(message = "A constelação é obrigatória")
    @Enumerated(EnumType.STRING)
    private ConstelacaoEnum constelacao;

    @NotNull(message = "A luminosidade é obrigatória")
    @Enumerated(EnumType.STRING)
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
