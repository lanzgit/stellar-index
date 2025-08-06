package br.edu.infnet.stellarindexapi.model.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Astro {
    private Integer id;
    private String nome;
    private double temperaturaMedia;
    private String descricao;
    private boolean ehHabitavel;

    @Override
    public String toString() {
        return String.format("%d - %s - %.2f - %s - %b",
                id, nome, temperaturaMedia, descricao, ehHabitavel);
    }

    public abstract String obterTipo();
}
