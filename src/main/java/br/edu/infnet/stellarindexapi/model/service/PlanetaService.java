package br.edu.infnet.stellarindexapi.model.service;

import br.edu.infnet.stellarindexapi.model.domain.Planeta;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class PlanetaService implements CrudService<Planeta, Integer>{

    private final Map<Integer, Planeta> mapa = new ConcurrentHashMap<Integer, Planeta>();
    private final AtomicInteger nextId = new AtomicInteger(1);

    @Override
    public Planeta salvar(Planeta planeta) {
        planeta.setId(nextId.getAndIncrement());
        mapa.put(planeta.getPlanetaId(), planeta);

        return planeta;
    }

    @Override
    public Planeta obter(Integer id) {

        return null;
    }

    @Override
    public void excluir(Integer id) {

    }

    @Override
    public List<Planeta> obterTodos() {

        //outra abordagem
        List<Planeta> planetas = new ArrayList<Planeta>();

        return List.of((Planeta) mapa.values());
    }

    @Override
    public Planeta atualizar(Planeta entity) {
        return null;
    }
}
