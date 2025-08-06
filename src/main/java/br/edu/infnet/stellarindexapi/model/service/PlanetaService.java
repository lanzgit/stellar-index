package br.edu.infnet.stellarindexapi.model.service;

import br.edu.infnet.stellarindexapi.model.domain.Planeta;
import br.edu.infnet.stellarindexapi.model.domain.exceptions.PlanetaInvalidoException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        if (planeta.getNome() == null) {
            throw new PlanetaInvalidoException("O nome do planeta é um informação obrigatória");
        }
        planeta.setId(nextId.getAndIncrement());
        mapa.put(planeta.getId(), planeta);

        return planeta;
    }

    @Override
    public Planeta obterPorId(Integer id) {
        Planeta planeta = mapa.get(id);
        if (!mapa.containsKey(id)) {
            throw new PlanetaInvalidoException("O planeta com o ID: " + id + " não existe");
        }

        return planeta;
    }

    @Override
    public void excluir(Integer id) {
        if (!mapa.containsKey(id)) {
            throw new PlanetaInvalidoException("O planeta com o ID: " + id + " não existe");
        }
        mapa.remove(id);
    }

    @Override
    public List<Planeta> obterTodos() {
        return new ArrayList<Planeta>(mapa.values());
    }

    @Override
    public Planeta atualizar(Planeta planeta, Integer id) {
        if (!mapa.containsKey(id)) {
            throw new PlanetaInvalidoException("O planeta com o ID: " + id + " não existe");
        }
        if (planeta.getNome() == null) {
            throw new PlanetaInvalidoException("O nome do planeta é um informação obrigatória");
        }
        //FIXME: id ficando nulo ao usar o put
        this.mapa.put(id, planeta);

        return planeta;
    }
}
