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

    private void validar(Planeta planeta) {
        if (planeta == null) {
            throw new IllegalArgumentException("O planeta não pode ser nulo");
        }
        if (planeta.getNome() == null || planeta.getNome().trim().isEmpty()) {
            throw new PlanetaInvalidoException("O nome do planeta é um informação obrigatória");
        }
    }

    @Override
    public Planeta salvar(Planeta planeta) {
        this.validar(planeta);
        if (planeta.getId() != null && planeta.getId() != 0) {
            throw new IllegalArgumentException("Um novo planeta não pode ter um ID na inclusão");
        }

        planeta.setId(nextId.getAndIncrement());
        mapa.put(planeta.getId(), planeta);

        return planeta;
    }

    @Override
    public Planeta obterPorId(Integer id) {
        Planeta planeta = mapa.get(id);
        if (planeta == null) {
            throw new IllegalArgumentException("Impossível obter o planeta pelo ID: " + id);
        }

        return planeta;
    }

    @Override
    public void excluir(Integer id) {
        if (!mapa.containsKey(id) && id != null) {
            throw new IllegalArgumentException("id inválido");
        }
        mapa.remove(id);
    }

    @Override
    public List<Planeta> obterTodos() {
        return new ArrayList<Planeta>(mapa.values());
    }

    @Override
    public Planeta atualizar(Planeta planeta, Integer id) {
        this.validar(planeta);
        if (!mapa.containsKey(id)) {
            throw new IllegalArgumentException("Id inexistente");
        }

        this.obterPorId(id);
        planeta.setId(id);
        this.mapa.put(planeta.getId(), planeta);

        return planeta;
    }

    public Planeta nuke(Integer id) {

        if (id == null || id == 0) {
            throw new IllegalArgumentException("o ID não pode ser nulo ou zero");
        }
        if (!mapa.containsKey(id)) {
            throw new IllegalArgumentException("Id inexistente");
        }

        Planeta planeta = this.obterPorId(id);
        if (!planeta.isEhHabitavel()) {
            System.out.println("O planeta " + planeta.getNome() + "já não é mais habitável");
            return planeta;
        }

        planeta.setEhHabitavel(false);

        return planeta;
    }
}
