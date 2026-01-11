package br.edu.infnet.stellarindexapi.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import br.edu.infnet.stellarindexapi.model.domain.Lua;
import br.edu.infnet.stellarindexapi.model.dto.LuaDTO;
import br.edu.infnet.stellarindexapi.model.dto.LuaMapper;
import br.edu.infnet.stellarindexapi.model.service.LuaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Tag(name = "Luas", description = "Gerenciador de Luas")
@RequiredArgsConstructor
public class LuaController {

  private final LuaService luaService;
  private final LuaMapper luaMapper;

  @GetMapping("/luas")
  public ResponseEntity<List<LuaDTO>> obterLuas() {
    List<Lua> luas = this.luaService.obterTodos();
    List<LuaDTO> luasDTO = luas.stream().map(luaMapper::toDTO).collect(Collectors.toList());
    return ResponseEntity.status(OK).body(luasDTO);
  }

  @GetMapping("/lua/{id}")
  public ResponseEntity<LuaDTO> obterLuaPorId(@PathVariable Integer id) {
    Lua lua = this.luaService.obterPorId(id);
    return ResponseEntity.status(OK).body(luaMapper.toDTO(lua));
  }

  @PostMapping("/lua")
  public ResponseEntity<LuaDTO> criarLua(@Valid @RequestBody LuaDTO luaDTO) {
    Lua lua = luaMapper.toEntity(luaDTO);
    Lua novaLua = this.luaService.criar(lua);
    return ResponseEntity.status(CREATED).body(luaMapper.toDTO(novaLua));
  }

  @PutMapping("/lua/{id}")
  public ResponseEntity<LuaDTO> atualizarLua(
      @PathVariable Integer id, @Valid @RequestBody LuaDTO luaDTO) {
    Lua lua = luaMapper.toEntity(luaDTO);
    Lua luaAtualizada = this.luaService.atualizar(lua, id);
    return ResponseEntity.status(OK).body(luaMapper.toDTO(luaAtualizada));
  }

  @DeleteMapping("lua/{id}")
  public ResponseEntity<Void> excluirLua(@PathVariable Integer id) {
    this.luaService.excluir(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("luas/{planetaId}")
  public ResponseEntity<List<LuaDTO>> obterLuasPorPlanetaId(@PathVariable Integer planetaId) {
    List<Lua> luas = this.luaService.obterPorPlanetaId(planetaId);

    if (luas.isEmpty()) {
      return ResponseEntity.noContent().build();
    }

    List<LuaDTO> luasDTO = luas.stream().map(luaMapper::toDTO).collect(Collectors.toList());
    return ResponseEntity.status(OK).body(luasDTO);
  }

  @GetMapping("/luas/distancia/{distancia}")
  public ResponseEntity<List<LuaDTO>> obterLuasComDistanciaOrbitralMaiorQue(
      @PathVariable Double distancia) {
    List<Lua> luas = this.luaService.obterLuasComDistanciaOrbitralMaiorQue(distancia);

    if (luas.isEmpty()) {
      return ResponseEntity.noContent().build();
    }

    List<LuaDTO> luasDTO = luas.stream().map(luaMapper::toDTO).collect(Collectors.toList());
    return ResponseEntity.status(OK).body(luasDTO);
  }
}
