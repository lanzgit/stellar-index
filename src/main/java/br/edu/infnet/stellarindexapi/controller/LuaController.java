package br.edu.infnet.stellarindexapi.controller;

import br.edu.infnet.stellarindexapi.model.domain.Lua;
import br.edu.infnet.stellarindexapi.model.dto.LuaDTO;
import br.edu.infnet.stellarindexapi.model.dto.LuaMapper;
import br.edu.infnet.stellarindexapi.model.service.LuaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api")
@Tag(name = "Luas", description = "Gerenciador de Luas")
public class LuaController {
    private final LuaService luaService;

    public LuaController(LuaService luaService) {
        this.luaService = luaService;
    }

    @GetMapping("/luas")
    public ResponseEntity<List<LuaDTO>> obterLuas() {
        List<Lua> luas = this.luaService.obterTodos();
        List<LuaDTO> luasDTO = luas.stream()
                .map(LuaMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.status(OK).body(luasDTO);
    }

    @GetMapping("/lua/{id}")
    public ResponseEntity<LuaDTO> obterLuaPorId(@PathVariable Integer id) {
        Lua lua = this.luaService.obterPorId(id);
        return ResponseEntity.status(OK).body(LuaMapper.toDTO(lua));
    }

    @PostMapping("/lua")
    public ResponseEntity<LuaDTO> criarLua(@Valid @RequestBody LuaDTO luaDTO) {
        Lua lua = LuaMapper.toEntity(luaDTO);
        Lua novaLua = this.luaService.criar(lua);
        return ResponseEntity.status(CREATED).body(LuaMapper.toDTO(novaLua));
    }

    @PutMapping("/lua/{id}")
    public ResponseEntity<LuaDTO> atualizarLua(@PathVariable Integer id, @Valid @RequestBody LuaDTO luaDTO) {
        Lua lua = LuaMapper.toEntity(luaDTO);
        Lua luaAtualizada = this.luaService.atualizar(lua, id);
        return ResponseEntity.status(OK).body(LuaMapper.toDTO(luaAtualizada));
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
        
        List<LuaDTO> luasDTO = luas.stream()
                .map(LuaMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.status(OK).body(luasDTO);
    }

    @GetMapping("/luas/distancia/{distancia}")
    public ResponseEntity<List<LuaDTO>> obterLuasComDistanciaOrbitralMaiorQue(@PathVariable Double distancia) {
        List<Lua> luas = this.luaService.obterLuasComDistanciaOrbitralMaiorQue(distancia);

        if (luas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<LuaDTO> luasDTO = luas.stream()
                .map(LuaMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.status(OK).body(luasDTO);
    }
}
