package br.edu.infnet.stellarindexapi;

import br.edu.infnet.stellarindexapi.model.domain.Planeta;
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
@Order(1)
public class PlanetaLoader implements ApplicationRunner {
  private final PlanetaService planetaService;

  public PlanetaLoader(PlanetaService planetaService) {
    this.planetaService = planetaService;
  }

  @Override
  public void run(ApplicationArguments args) throws Exception {
    ClassPathResource resource = new ClassPathResource("data/planeta.txt");
    InputStream inputStream = resource.getInputStream();
    BufferedReader leitura = new BufferedReader(new InputStreamReader(inputStream));

    String linha = leitura.readLine();
    String[] campos = null;

    while (linha != null) {
      campos = linha.split(";");

      Planeta planeta = new Planeta();
      planeta.setNome(campos[0]);
      planeta.setTemperaturaMedia(Double.valueOf(campos[1]));
      planeta.setDescricao(campos[2]);
      planeta.setEhHabitavel(Boolean.valueOf(campos[3]));
      planeta.setGravidade(Double.valueOf(campos[4]));
      planeta.setTemSateliteNatural(Boolean.valueOf(campos[5]));

      try {
        this.planetaService.criar(planeta);
      } catch (Exception e) {
        System.err.println("Erro na criação: " + e.getMessage());
      }

      linha = leitura.readLine();
    }

    List<Planeta> planetas = this.planetaService.obterTodos();
    planetas.forEach(System.out::println);

    leitura.close();
  }
}
