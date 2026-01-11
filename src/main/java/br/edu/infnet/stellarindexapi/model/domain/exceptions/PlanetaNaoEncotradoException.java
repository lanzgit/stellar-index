package br.edu.infnet.stellarindexapi.model.domain.exceptions;

public class PlanetaNaoEncotradoException extends RuntimeException {

  public PlanetaNaoEncotradoException(String mensagem) {
    super(mensagem);
  }
}
