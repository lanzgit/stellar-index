package br.edu.infnet.stellarindexapi;

import br.edu.infnet.stellarindexapi.model.domain.Lua;
import br.edu.infnet.stellarindexapi.model.domain.Planeta;
import br.edu.infnet.stellarindexapi.model.service.LuaService;
import br.edu.infnet.stellarindexapi.model.service.PlanetaService;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
@Order(2)
public class LuaLoader implements ApplicationRunner {
  private final LuaService luaService;
  private final PlanetaService planetaService;

  public LuaLoader(LuaService luaService, PlanetaService planetaService) {
    this.luaService = luaService;
    this.planetaService = planetaService;
  }

  @Override
  public void run(ApplicationArguments args) throws Exception {
    ClassPathResource resource = new ClassPathResource("data/lua.txt");
    InputStream inputStream = resource.getInputStream();
    BufferedReader leitura = new BufferedReader(new InputStreamReader(inputStream));

    String linha = leitura.readLine();
    String[] campos = null;

    while (linha != null) {
      campos = linha.split(";");

      Lua lua = new Lua();
      lua.setNome(campos[0]);
      lua.setTemperaturaMedia(Double.valueOf(campos[1]));
      lua.setDescricao(campos[2]);
      lua.setEhHabitavel(Boolean.valueOf(campos[3]));
      lua.setDistanciaOrbitral(Long.valueOf(campos[4]));

      Integer planetaId = Integer.valueOf(campos[5]);
      Planeta planeta = this.planetaService.obterPorId(planetaId);
      lua.setPlaneta(planeta);

      this.luaService.criar(lua);
      System.out.println(lua);
      linha = leitura.readLine();
    }
    List<Lua> luas = this.luaService.obterTodos();
    luas.forEach(System.out::println);

    leitura.close();
  }
}
