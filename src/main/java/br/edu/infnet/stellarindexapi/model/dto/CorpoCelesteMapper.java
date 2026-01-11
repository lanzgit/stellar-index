package br.edu.infnet.stellarindexapi.model.dto;

import br.edu.infnet.stellarindexapi.model.domain.CorpoCeleste;
import java.util.Optional;

public class CorpoCelesteMapper {

  public static CorpoCelesteDTO toDTO(CorpoCeleste corpoCeleste) {
    return Optional.ofNullable(corpoCeleste)
        .map(
            cc ->
                new CorpoCelesteDTO(
                    cc.getNome(),
                    cc.getNomeCompleto(),
                    cc.getClassificacaoOrbital(),
                    formatarPeriodoOrbital(cc.getPeriodoOrbital()),
                    formatarDistanciaMinima(cc.getDistanciaMinima()),
                    cc.getTipo(),
                    cc.ehPHA(),
                    cc.ehNEO()))
        .orElse(null);
  }

  public static CorpoCelesteDTO of(CorpoCeleste corpoCeleste) {
    return toDTO(corpoCeleste);
  }

  private static String formatarPeriodoOrbital(String periodoOrbital) {
    if (periodoOrbital == null || periodoOrbital.trim().isEmpty()) {
      return periodoOrbital;
    }

    if (periodoOrbital.toLowerCase().contains("dias")
        || periodoOrbital.toLowerCase().contains("days")) {
      return periodoOrbital;
    }

    try {
      double periodo = Double.parseDouble(periodoOrbital.trim());
      return String.format("%.2f dias", periodo);
    } catch (NumberFormatException e) {
      return periodoOrbital;
    }
  }

  private static String formatarDistanciaMinima(String distanciaMinima) {
    if (distanciaMinima == null || distanciaMinima.trim().isEmpty()) {
      return distanciaMinima;
    }

    if (distanciaMinima.toLowerCase().contains("au")
        || distanciaMinima.toLowerCase().contains("ua")) {
      return distanciaMinima;
    }

    try {
      double distancia = Double.parseDouble(distanciaMinima.trim());
      return String.format("%.6f AU", distancia);
    } catch (NumberFormatException e) {
      return distanciaMinima;
    }
  }
}
