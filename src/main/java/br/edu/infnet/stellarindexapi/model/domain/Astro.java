package br.edu.infnet.stellarindexapi.model.domain;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class Astro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(min = 2, max = 15)
    private String nome;

    private double temperaturaMedia;

    @NotBlank(message = "a descrição é obrigatória")
    @Size(min = 3, max = 100, message = "descrição deve ter no mínimo 3 caracteres e no máximo 100 caracteres.")
    private String descricao;

    private boolean ehHabitavel;

    @Override
    public String toString() {
        return String.format("%d - %s - %.2f - %s - %b",
                id, nome, temperaturaMedia, descricao, ehHabitavel);
    }

    public abstract String obterTipo();
}
