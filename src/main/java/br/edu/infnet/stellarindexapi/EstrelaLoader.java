package br.edu.infnet.stellarindexapi;

import br.edu.infnet.stellarindexapi.model.domain.Estrela;
import br.edu.infnet.stellarindexapi.model.enums.ConstelacaoEnum;
import br.edu.infnet.stellarindexapi.model.enums.LuminosidadeEnum;
import br.edu.infnet.stellarindexapi.model.service.EstrelaService;
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
@Order(3)
public class EstrelaLoader implements ApplicationRunner {
  private final EstrelaService estrelaService;

  public EstrelaLoader(EstrelaService estrelaService) {
    this.estrelaService = estrelaService;
  }

  @Override
  public void run(ApplicationArguments args) throws Exception {
    ClassPathResource resource = new ClassPathResource("data/estrela.txt");
    InputStream inputStream = resource.getInputStream();
    BufferedReader leitura = new BufferedReader(new InputStreamReader(inputStream));

    String linha = leitura.readLine();
    String[] campos = null;

    while (linha != null) {
      campos = linha.split(";");

      Estrela estrela = new Estrela();
      estrela.setNome(campos[0]);
      estrela.setTemperaturaMedia(Double.valueOf(campos[1]));
      estrela.setDescricao(campos[2]);
      estrela.setEhHabitavel(Boolean.valueOf(campos[3]));
      estrela.setConstelacao(ConstelacaoEnum.valueOf(campos[4]));
      estrela.setLuminosidade(LuminosidadeEnum.valueOf(campos[5]));

      this.estrelaService.criar(estrela);
      System.out.println(estrela);
      linha = leitura.readLine();
    }

    List<Estrela> estrelas = this.estrelaService.obterTodos();
    estrelas.forEach(System.out::println);
    leitura.close();
  }
}
