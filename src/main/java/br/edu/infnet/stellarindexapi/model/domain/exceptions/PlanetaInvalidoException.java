package br.edu.infnet.stellarindexapi.model.domain.exceptions;

public class PlanetaInvalidoException extends RuntimeException {

  private static final long SerialVersionUID = 1L;

  public PlanetaInvalidoException(String mensagem) {
    super(mensagem);
  }
}
