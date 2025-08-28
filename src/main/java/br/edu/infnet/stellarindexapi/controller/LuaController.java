package br.edu.infnet.stellarindexapi.controller;

import br.edu.infnet.stellarindexapi.model.domain.Lua;
import br.edu.infnet.stellarindexapi.model.service.LuaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<List<Lua>> obterLuas() {
        List<Lua> luas = this.luaService.obterTodos();
        return ResponseEntity.status(OK).body(luas);
    }

    @GetMapping("/lua/{id}")
    public ResponseEntity<Lua> obterLuaPorId(@PathVariable Integer id) {
        Lua lua = this.luaService.obterPorId(id);
        return ResponseEntity.status(OK).body(lua);
    }

    @PostMapping("/lua")
    public ResponseEntity<Lua> criarLua(@RequestBody Lua lua) {
        Lua novaLua = this.luaService.criar(lua);
        return ResponseEntity.status(CREATED).body(novaLua);
    }

    @PutMapping("/lua/{id}")
    public ResponseEntity<Lua> atualizarLua(@PathVariable Integer id, @RequestBody Lua lua) {
        Lua luaAtualizada = this.luaService.atualizar(lua, id);
        return ResponseEntity.status(OK).body(luaAtualizada);
    }

    @DeleteMapping("lua/{id}")
    public ResponseEntity<Void> excluirLua(@PathVariable Integer id) {
       this.luaService.excluir(id);
       return ResponseEntity.noContent().build();
    }

    @GetMapping("luas/{planetaId}")
    public ResponseEntity<List<Lua>> obterLuasPorPlanetaId(@PathVariable Integer planetaId) {
        List<Lua> luas = this.luaService.obterPorPlanetaId(planetaId);

        if (luas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        
        return ResponseEntity.status(OK).body(luas);
    }

    @GetMapping("/luas/distancia/{distancia}")
    public ResponseEntity<List<Lua>> obterLuasComDistanciaOrbitralMaiorQue(@PathVariable Double distancia) {
        List<Lua> luas = this.luaService.obterLuasComDistanciaOrbitralMaiorQue(distancia);

        if (luas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.status(OK).body(luas);
    }
}
