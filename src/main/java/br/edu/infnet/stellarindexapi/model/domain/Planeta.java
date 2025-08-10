package br.edu.infnet.stellarindexapi.model.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class Planeta extends Astro {

    private double gravidade;
    private boolean temSateliteNatural;

    @OneToMany(mappedBy = "planeta", cascade = CascadeType.ALL)
    private List<Lua> luas;

    @Override
    public String obterTipo() {
        return "Planeta";
    }
}
