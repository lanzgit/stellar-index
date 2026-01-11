package br.edu.infnet.stellarindexapi.model.enums;

import lombok.Getter;

@Getter
public enum LuminosidadeEnum {
  HIPER_GIGANTE("Classe 0", "Hiper Gigante"),
  SUPER_GIGANTE("Classe I", "Super Gigante"),
  GIGANTE("Classe II", "Gigante"),
  GIGANTE_REGULAR("Classe III", "Gigante Regular"),
  SUBGIGANTE("Classe IV", "SubGigante"),
  SEQUENCIA_PRINCIPAL("Classe V", "Sequência Principal"),
  SUBANA("Classe VI", "Subanã"),
  ANA_BRANCA("Class VII", "Anã Branca");

  private final String classe;
  private final String nome;

  LuminosidadeEnum(String classe, String nome) {
    this.classe = classe;
    this.nome = nome;
  }
}
