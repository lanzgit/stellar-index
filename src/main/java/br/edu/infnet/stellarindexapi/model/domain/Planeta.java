package br.edu.infnet.stellarindexapi.model.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Getter
@Setter
@Entity
public class Planeta extends Astro {

    private double gravidade;
    private boolean temSateliteNatural;

    @OneToMany(mappedBy = "planeta", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Lua> luas = new ArrayList<Lua>();

    @Override
    public String obterTipo() {
        return "Planeta";
    }
}
