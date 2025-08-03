package br.edu.infnet.stellarindexapi.model.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Planeta extends Astro {
    private Integer planetaId;
    private double gravidade;
    private boolean temSateliteNatural;
    private List<Lua> luas;
}
