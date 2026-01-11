package br.edu.infnet.stellarindexapi.model.dto;

import br.edu.infnet.stellarindexapi.model.enums.ConstelacaoEnum;
import br.edu.infnet.stellarindexapi.model.enums.LuminosidadeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record EstrelaDTO(
    Integer id,
    @Size(min = 2, max = 19) String nome,
    double temperaturaMedia,
    @NotBlank(message = "a descrição é obrigatória")
        @Size(
            min = 3,
            max = 100,
            message = "descrição deve ter no mínimo 3 caracteres e no máximo 100 caracteres.")
        String descricao,
    boolean ehHabitavel,
    @NotNull(message = "A constelação é obrigatória") ConstelacaoEnum constelacao,
    @NotNull(message = "A luminosidade é obrigatória") LuminosidadeEnum luminosidade) {}
