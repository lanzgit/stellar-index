package br.edu.infnet.stellarindexapi.model.service;


import java.util.List;

public interface CrudService<T, ID> {

    List<T> obterTodos();

    T obter(ID id);

    T salvar(T entity);

    T atualizar(T entity);

    void excluir(ID id);
}
