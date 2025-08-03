package br.edu.infnet.stellarindexapi.model.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Astro {
    private Integer id;
    private String nome;
    private String tipoAstro;
    private int numeroLuas;
    private double temperaturaMedia;
    private String descricao;
    private boolean ehHabitavel;
}
