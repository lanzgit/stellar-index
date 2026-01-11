package br.edu.infnet.stellarindexapi.model.dto;

import br.edu.infnet.stellarindexapi.model.domain.Planeta;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlanetaMapper {

  private final LuaMapper luaMapper;

  public PlanetaDTO toDTO(Planeta planeta) {
    if (planeta == null) {
      return null;
    }

    List<LuaDTO> luasDTO =
        planeta.getLuas() != null
            ? planeta.getLuas().stream().map(luaMapper::toDTO).collect(Collectors.toList())
            : Collections.emptyList();

    return new PlanetaDTO(
        planeta.getId(),
        planeta.getNome(),
        planeta.getTemperaturaMedia(),
        planeta.getDescricao(),
        planeta.isEhHabitavel(),
        planeta.getGravidade(),
        planeta.isTemSateliteNatural(),
        luasDTO);
  }

  public Planeta toEntity(PlanetaDTO dto) {
    if (dto == null) {
      return null;
    }
    Planeta planeta = new Planeta();
    planeta.setId(dto.id());
    planeta.setNome(dto.nome());
    planeta.setTemperaturaMedia(dto.temperaturaMedia());
    planeta.setDescricao(dto.descricao());
    planeta.setEhHabitavel(dto.ehHabitavel());
    planeta.setGravidade(dto.gravidade());
    planeta.setTemSateliteNatural(dto.temSateliteNatural());

    if (dto.luas() != null && !dto.luas().isEmpty()) {
      planeta.setLuas(
          dto.luas().stream()
              .map(luaMapper::toEntity)
              .peek(lua -> lua.setPlaneta(planeta))
              .collect(Collectors.toList()));
    }

    return planeta;
  }
}
