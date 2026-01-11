package br.edu.infnet.stellarindexapi.model.dto;

import br.edu.infnet.stellarindexapi.model.domain.Lua;

public class LuaMapper {

  public static LuaDTO toDTO(Lua lua) {
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

  public static Lua toEntity(LuaDTO dto) {
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
    return lua;
  }
}
