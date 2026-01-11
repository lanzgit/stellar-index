package br.edu.infnet.stellarindexapi.model.dto;

import br.edu.infnet.stellarindexapi.model.domain.Lua;
import br.edu.infnet.stellarindexapi.model.domain.Planeta;
import br.edu.infnet.stellarindexapi.model.service.PlanetaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LuaMapper {

  private final PlanetaService planetaService;

  public LuaDTO toDTO(Lua lua) {
    if (lua == null) {
      return null;
    }
    return new LuaDTO(
        lua.getId(),
        lua.getNome(),
        lua.getTemperaturaMedia(),
        lua.getDescricao(),
        lua.isEhHabitavel(),
        lua.getDistanciaOrbitral(),
        lua.getPlaneta() != null ? lua.getPlaneta().getId() : null,
        lua.getPlaneta() != null ? lua.getPlaneta().getNome() : null);
  }

  public Lua toEntity(LuaDTO dto) {
    if (dto == null) {
      return null;
    }
    Lua lua = new Lua();
    lua.setId(dto.id());
    lua.setNome(dto.nome());
    lua.setTemperaturaMedia(dto.temperaturaMedia());
    lua.setDescricao(dto.descricao());
    lua.setEhHabitavel(dto.ehHabitavel());
    lua.setDistanciaOrbitral(dto.distanciaOrbitral());

    if (dto.planetaId() != null) {
      Planeta planeta = planetaService.obterPorId(dto.planetaId());
      lua.setPlaneta(planeta);
    }

    return lua;
  }
}
