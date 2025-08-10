package br.edu.infnet.stellarindexapi.model.service;

import br.edu.infnet.stellarindexapi.model.domain.Estrela;
import br.edu.infnet.stellarindexapi.model.domain.Lua;
import br.edu.infnet.stellarindexapi.model.domain.Planeta;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class EstrelaService implements CrudService<Estrela, Integer> {

    private final Map<Integer, Estrela> mapa = new ConcurrentHashMap<Integer, Estrela>();
    private final AtomicInteger nextId = new AtomicInteger(1);

    @Override
    public List<Estrela> obterTodos() {
        return List.of();
    }

    @Override
    public Estrela obterPorId(Integer integer) {
        return null;
    }

    @Override
    public Estrela criar(Estrela entity) {
        return null;
    }

    @Override
    public Estrela atualizar(Estrela entity, Integer integer) {
        return null;
    }

    @Override
    public void excluir(Integer integer) {

    }
}
