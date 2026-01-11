package br.edu.infnet.stellarindexapi.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LuaDTO(
    Integer id,
    @Size(min = 2, max = 15) String nome,
    double temperaturaMedia,
    @NotBlank(message = "a descrição é obrigatória")
        @Size(
            min = 3,
            max = 100,
            message = "descrição deve ter no mínimo 3 caracteres e no máximo 100 caracteres.")
        String descricao,
    boolean ehHabitavel,
    @NotNull(message = "A distância orbital é obrigatória") Long distanciaOrbitral,
    Integer planetaId,
    String planetaNome) {}
