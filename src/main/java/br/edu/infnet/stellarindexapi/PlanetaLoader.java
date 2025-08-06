package br.edu.infnet.stellarindexapi;

import br.edu.infnet.stellarindexapi.model.domain.Lua;
import br.edu.infnet.stellarindexapi.model.domain.Planeta;
import br.edu.infnet.stellarindexapi.model.service.PlanetaService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;

@Component
public class PlanetaLoader implements ApplicationRunner {
    private final PlanetaService planetaService;

    public PlanetaLoader(PlanetaService planetaService) {
        this.planetaService = planetaService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        FileReader arquivo = new FileReader("planeta.txt");
        BufferedReader leutira = new BufferedReader(arquivo);

        String linha = leutira.readLine();
        String[] campos = null;

        while (linha != null) {
            campos = linha.split(";");

            Lua lua = new Lua();
            //TODO: adicionar ID incremental na lua também
            lua.setId(Integer.valueOf(campos[3]));
            lua.setNome(campos[4]);
            lua.setDistanciaOrbitral(Long.valueOf(campos[5]));

            Planeta terra = new Planeta();
            terra.setNome(campos[0]);
            terra.setGravidade(Double.valueOf(campos[1]));
            terra.setTemSateliteNatural(Boolean.valueOf(campos[2]));
            terra.setLuas(List.of(lua));

            this.planetaService.salvar(terra);
            System.out.println(terra);
            linha = leutira.readLine();
        }
        System.out.println("- " + this.planetaService.obterTodos().size());
        leutira.close();
    }
}
