package br.edu.infnet.stellarindexapi.model.service;

import br.edu.infnet.stellarindexapi.model.domain.Lua;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class LuaService implements CrudService<Lua, Integer> {

    private final Map<Integer, Lua> mapa = new ConcurrentHashMap<Integer, Lua>();
    private final AtomicInteger nextId = new AtomicInteger(1);
    private final ValidacaoService validacaoService;

    public LuaService(ValidacaoService validacaoService) {
        this.validacaoService = validacaoService;
    }

    @Override
    public List<Lua> obterTodos() {
        return new ArrayList<Lua>(this.mapa.values());
    }

    @Override
    public Lua obterPorId(Integer id) {
        Lua lua = mapa.get(id);
        if (Objects.isNull(lua)) {
            throw new IllegalArgumentException("Impossível obter a lua pelo ID: " + id);
        }

        return lua;
    }

    @Override
    public Lua criar(Lua lua) {
        this.validacaoService.validarAstro(lua, lua.getNome());
        if (lua.getId() != null && lua.getId() != 0) {
            throw  new IllegalArgumentException("uma nova lua não pode ter um ID na inclusão");
        }

        lua.setId(nextId.getAndIncrement());
        mapa.put(lua.getId(), lua);

        return lua;
    }

    @Override
    public Lua atualizar(Lua lua, Integer id) {
        this.validacaoService.validarAstro(lua, lua.getNome());
        if (!mapa.containsKey(id)) {
            throw new IllegalArgumentException("Id inexistente");
        }
        this.obterPorId(id);
        lua.setId(id);
        this.mapa.put(lua.getId(), lua);

        return lua;
    }

    @Override
    public void excluir(Integer id) {
        if (!mapa.containsKey(id) && id != null) {
            throw new IllegalArgumentException("id inválido");
        }

        this.mapa.remove(id);
    }
}
