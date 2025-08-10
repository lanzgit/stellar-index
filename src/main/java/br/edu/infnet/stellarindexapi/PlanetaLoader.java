package br.edu.infnet.stellarindexapi;

import br.edu.infnet.stellarindexapi.model.domain.Planeta;
import br.edu.infnet.stellarindexapi.model.service.PlanetaService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;

@Component
@Order(1)
public class PlanetaLoader implements ApplicationRunner {
    private final PlanetaService planetaService;

    public PlanetaLoader(PlanetaService planetaService) {
        this.planetaService = planetaService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        FileReader arquivo = new FileReader("planeta.txt");
        BufferedReader leitura = new BufferedReader(arquivo);

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

            this.planetaService.criar(planeta);
            System.out.println(planeta);
            linha = leitura.readLine();
        }
        
        System.out.println("### " + this.planetaService.obterTodos().size() + " planetas carregados");
        leitura.close();
    }
}
