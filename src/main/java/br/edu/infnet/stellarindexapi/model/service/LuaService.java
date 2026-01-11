package br.edu.infnet.stellarindexapi.model.service;

import br.edu.infnet.stellarindexapi.model.domain.Lua;
import br.edu.infnet.stellarindexapi.model.domain.exceptions.LuaNaoEncontradaException;
import br.edu.infnet.stellarindexapi.model.repository.LuaRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

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

    return this.luaRepository
        .findById(id)
        .orElseThrow(() -> new LuaNaoEncontradaException("Lua não encontrada"));
  }

  @Override
  public Lua criar(Lua lua) {
    this.validacaoService.validarAstro(lua, lua.getNome());
    if (lua.getId() != null && lua.getId() != 0) {
      throw new IllegalArgumentException("uma nova lua não pode ter um ID na inclusão");
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

    return this.luaRepository.save(luaExistente);
  }

  @Override
  public void excluir(Integer id) {
    Lua lua = this.obterPorId(id);
    this.luaRepository.delete(lua);
  }

  public List<Lua> obterPorPlanetaId(Integer planetaId) {
    if (planetaId == null || planetaId <= 0) {
      throw new IllegalArgumentException("O id do planeta não pode ser nulo ou menor que zero");
    }

    return this.luaRepository.findByPlanetaId(planetaId).orElse(new ArrayList<>());
  }

  public List<Lua> obterLuasComDistanciaOrbitralMaiorQue(Double distancia) {
    if (distancia == null || distancia < 0) {
      throw new IllegalArgumentException("A distância não pode ser nula ou negativa");
    }

    return this.luaRepository.findByDistanciaOrbitralGreaterThan(distancia);
  }
}
