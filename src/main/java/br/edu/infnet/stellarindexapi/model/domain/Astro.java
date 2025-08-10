package br.edu.infnet.stellarindexapi.model.domain;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class Astro {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
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
