package br.edu.infnet.stellarindexapi.model.domain.exceptions;

public class LuaNaoEncontradaException extends RuntimeException {
    public LuaNaoEncontradaException(String mensagem) {
        super(mensagem);
    }
}
