package br.edu.infnet.stellarindexapi.model.dto;

public record CorpoCelesteDTO(
        String nome,
        String nomeCompleto,
        String classificacaoOrbital,
        String periodoOrbital,
        String distanciaMinima,
        String tipo,
        boolean ehPerigoso,
        boolean ehNEO
) {
}