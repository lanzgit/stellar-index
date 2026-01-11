package br.edu.infnet.stellarindexapi.model.dto;

import br.edu.infnet.stellarindexapi.model.domain.Asteroide;

public class AsteroideMapper {

  public static AsteroideDTO toDTO(Asteroide asteroide) {
    if (asteroide == null) {
      return null;
    }
    return new AsteroideDTO(
        asteroide.getId(),
        asteroide.getNome(),
        asteroide.getTemperaturaMedia(),
        asteroide.getDescricao(),
        asteroide.isEhHabitavel(),
        asteroide.getDesignacao(),
        asteroide.getNomeCompleto(),
        asteroide.getClassificacaoOrbital(),
        asteroide.getPeriodoOrbital(),
        asteroide.getTipo(),
        asteroide.isNeo(),
        asteroide.isPha());
  }

  public static Asteroide toEntity(AsteroideDTO dto) {
    if (dto == null) {
      return null;
    }
    Asteroide asteroide = new Asteroide();
    asteroide.setId(dto.id());
    asteroide.setNome(dto.nome());
    asteroide.setTemperaturaMedia(dto.temperaturaMedia());
    asteroide.setDescricao(dto.descricao());
    asteroide.setEhHabitavel(dto.ehHabitavel());
    asteroide.setDesignacao(dto.designacao());
    asteroide.setNomeCompleto(dto.nomeCompleto());
    asteroide.setClassificacaoOrbital(dto.classificacaoOrbital());
    asteroide.setPeriodoOrbital(dto.periodoOrbital());
    asteroide.setTipo(dto.tipo());
    asteroide.setNeo(dto.neo());
    asteroide.setPha(dto.pha());
    return asteroide;
  }
}
