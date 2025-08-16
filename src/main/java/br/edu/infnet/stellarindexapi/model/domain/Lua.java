package br.edu.infnet.stellarindexapi.model.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Lua extends Astro{

    private Long distanciaOrbitral;

    @ManyToOne
    @JoinColumn(name = "planeta_id", nullable = false)
    @JsonBackReference
    private Planeta planeta;

    @Override
    public String obterTipo() {
        return "Lua";
    }
}
