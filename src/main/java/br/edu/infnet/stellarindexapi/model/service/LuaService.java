package br.edu.infnet.stellarindexapi.model.service;

import br.edu.infnet.stellarindexapi.model.domain.Lua;
import br.edu.infnet.stellarindexapi.model.domain.exceptions.LuaNaoEncontradaException;
import br.edu.infnet.stellarindexapi.model.repository.LuaRepository;
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

    private final ValidacaoService validacaoService;
    private final LuaRepository luaRepository;

    public LuaService(ValidacaoService validacaoService, LuaRepository luaRepository) {
        this.validacaoService = validacaoService;
        this.luaRepository = luaRepository;
    }

    @Override
    public List<Lua> obterTodos() {
        return this.luaRepository.findAll();
    }

    @Override
    public Lua obterPorId(Integer id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("o id não pode ser nulo ou zero");
        }

        return this.luaRepository.findById(id)
                .orElseThrow(() -> new LuaNaoEncontradaException("Lua não encontrada"));
    }

    @Override
    public Lua criar(Lua lua) {
        this.validacaoService.validarAstro(lua, lua.getNome());
        if (lua.getId() != null && lua.getId() != 0) {
            throw  new IllegalArgumentException("uma nova lua não pode ter um ID na inclusão");
        }

        return this.luaRepository.save(lua);
    }

    @Override
    public Lua atualizar(Lua lua, Integer id) {
        this.validacaoService.validarAstro(lua, lua.getNome());

        Lua luaExistente = this.obterPorId(id);
        luaExistente.setNome(lua.getNome());
        luaExistente.setDistanciaOrbitral(lua.getDistanciaOrbitral());
        luaExistente.setDescricao(lua.getDescricao());
        luaExistente.setEhHabitavel(lua.isEhHabitavel());
        luaExistente.setTemperaturaMedia(lua.getTemperaturaMedia());
        luaExistente.setPlaneta(lua.getPlaneta());

        return this.luaRepository.save(luaExistente);
    }

    @Override
    public void excluir(Integer id) {
        Lua lua = this.obterPorId(id);
        this.luaRepository.delete(lua);
    }
}
