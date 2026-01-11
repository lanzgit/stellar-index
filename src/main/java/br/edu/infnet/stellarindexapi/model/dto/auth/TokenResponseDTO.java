package br.edu.infnet.stellarindexapi.model.dto.auth;

public record TokenResponseDTO(String token, String tipo, String username, String papel) {}
