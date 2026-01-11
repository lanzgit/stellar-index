package br.edu.infnet.stellarindexapi.model.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Planeta extends Astro {

  private double gravidade;
  private boolean temSateliteNatural;

  @OneToMany(
      mappedBy = "planeta",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.LAZY)
  @JsonManagedReference
  private List<Lua> luas = new ArrayList<Lua>();

  @Override
  public String obterTipo() {
    return "Planeta";
  }
}
