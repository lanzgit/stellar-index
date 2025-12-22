package br.edu.infnet.stellarindexapi.model.dto;

import br.edu.infnet.stellarindexapi.model.domain.Planeta;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PlanetaMapper {

    public static PlanetaDTO toDTO(Planeta planeta) {
        if (planeta == null) {
            return null;
        }

        List<LuaDTO> luasDTO = planeta.getLuas() != null
                ? planeta.getLuas().stream()
                    .map(lua -> new LuaDTO(
                            lua.getId(),
                            lua.getNome(),
                            lua.getTemperaturaMedia(),
                            lua.getDescricao(),
                            lua.isEhHabitavel(),
                            lua.getDistanciaOrbitral(),
                            planeta.getId(),
                            planeta.getNome()
                    ))
                    .collect(Collectors.toList())
                : Collections.emptyList();

        return new PlanetaDTO(
                planeta.getId(),
                planeta.getNome(),
                planeta.getTemperaturaMedia(),
                planeta.getDescricao(),
                planeta.isEhHabitavel(),
                planeta.getGravidade(),
                planeta.isTemSateliteNatural(),
                luasDTO
        );
    }

    public static Planeta toEntity(PlanetaDTO dto) {
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
                    .map(LuaMapper::toEntity)
                    .peek(lua -> lua.setPlaneta(planeta))
                    .collect(Collectors.toList())
            );
        }

        return planeta;
    }
}

