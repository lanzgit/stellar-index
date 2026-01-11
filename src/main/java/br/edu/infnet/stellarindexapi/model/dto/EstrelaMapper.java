package br.edu.infnet.stellarindexapi.model.dto;

import br.edu.infnet.stellarindexapi.model.domain.Estrela;

public class EstrelaMapper {

  public static EstrelaDTO toDTO(Estrela estrela) {
    if (estrela == null) {
      return null;
    }
    return new EstrelaDTO(
        estrela.getId(),
        estrela.getNome(),
        estrela.getTemperaturaMedia(),
        estrela.getDescricao(),
        estrela.isEhHabitavel(),
        estrela.getConstelacao(),
        estrela.getLuminosidade());
  }

  public static Estrela toEntity(EstrelaDTO dto) {
    if (dto == null) {
      return null;
    }
    Estrela estrela = new Estrela();
    estrela.setId(dto.id());
    estrela.setNome(dto.nome());
    estrela.setTemperaturaMedia(dto.temperaturaMedia());
    estrela.setDescricao(dto.descricao());
    estrela.setEhHabitavel(dto.ehHabitavel());
    estrela.setConstelacao(dto.constelacao());
    estrela.setLuminosidade(dto.luminosidade());
    return estrela;
  }
}
