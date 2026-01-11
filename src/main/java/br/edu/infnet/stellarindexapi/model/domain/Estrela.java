package br.edu.infnet.stellarindexapi.model.domain;

import br.edu.infnet.stellarindexapi.model.enums.ConstelacaoEnum;
import br.edu.infnet.stellarindexapi.model.enums.LuminosidadeEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Estrela extends Astro {

  @NotNull(message = "A constelação é obrigatória")
  @Enumerated(EnumType.STRING)
  private ConstelacaoEnum constelacao;

  @NotNull(message = "A luminosidade é obrigatória")
  @Enumerated(EnumType.STRING)
  private LuminosidadeEnum luminosidade;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @PrePersist
  protected void onCreate() {
    updatedAt = LocalDateTime.now();
  }

  @PreUpdate
  protected void onUpdate() {
    updatedAt = LocalDateTime.now();
  }

  @Override
  public String obterTipo() {
    return "Estrela";
  }

  @Override
  public String toString() {
    return String.format(
        "%s | %s - %s", constelacao, luminosidade.getNome(), luminosidade.getClasse());
  }
}
