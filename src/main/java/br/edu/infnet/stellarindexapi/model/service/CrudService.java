package br.edu.infnet.stellarindexapi.model.service;


import java.util.List;

public interface CrudService<T, ID> {

    List<T> obterTodos();

    T obterPorId(ID id);

    T salvar(T entity);

    T atualizar(T entity, ID id);

    void excluir(ID id);
}
