package br.edu.infnet.stellarindexapi.model.dto;

import br.edu.infnet.stellarindexapi.model.enums.ConstelacaoEnum;
import br.edu.infnet.stellarindexapi.model.enums.LuminosidadeEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class EstrelaDoc {

  private Integer id;
  private String nome;
  private double temperaturaMedia;
  private String descricao;
  private boolean ehHabitavel;
  private ConstelacaoEnum constelacao;
  private LuminosidadeEnum luminosidade;
  private Double score;
}
