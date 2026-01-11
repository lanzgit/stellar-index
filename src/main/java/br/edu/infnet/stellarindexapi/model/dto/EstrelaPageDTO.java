package br.edu.infnet.stellarindexapi.model.dto;

import java.util.List;

public record EstrelaPageDTO(List<EstrelaDTO> estrelas, long totalElements, int totalPages) {}
