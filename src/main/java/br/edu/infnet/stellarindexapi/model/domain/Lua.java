package br.edu.infnet.stellarindexapi.model.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Lua extends Astro{

    private Long distanciaOrbitral;

    @ManyToOne
    @JoinColumn(name = "planeta_id", nullable = false)
    private Planeta planeta;

    @Override
    public String obterTipo() {
        return "Lua";
    }
}
