package br.edu.infnet.stellarindexapi.model.domain.exceptions;

public class AstroInvalidoException extends RuntimeException {

    private static final long SerialVersionUID = 1L;

    public AstroInvalidoException(String mensagem) {
        super(mensagem);
    }
}
