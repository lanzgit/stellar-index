package br.edu.infnet.stellarindexapi;

import br.edu.infnet.stellarindexapi.model.domain.Asteroide;
import br.edu.infnet.stellarindexapi.model.service.AsteroideService;
import lombok.RequiredArgsConstructor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(4)
@RequiredArgsConstructor
public class AsteroideLoader implements ApplicationRunner {

    private final AsteroideService asteroideService;
    
    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("Carregando asteroides do arquivo asteroide.txt");
        
        FileReader arquivo = new FileReader("asteroide.txt");
        BufferedReader leitura = new BufferedReader(arquivo);

        String linha = leitura.readLine();
        String[] campos = null;

        while (linha != null) {
            campos = linha.split(";");

            Asteroide asteroide = new Asteroide();
            asteroide.setNome(campos[0]);
            asteroide.setTemperaturaMedia(Double.valueOf(campos[1]));
            asteroide.setDescricao(campos[2]);
            asteroide.setEhHabitavel(Boolean.valueOf(campos[3]));
            asteroide.setDesignacao(campos[4]);
            asteroide.setNeo(Boolean.valueOf(campos[5]));
            asteroide.setPha(Boolean.valueOf(campos[6]));

            try {
                this.asteroideService.criar(asteroide);
            } catch (Exception e) {
                System.err.println("Erro na criação do asteroide " + asteroide.getNome() + ": " + e.getMessage());
            }

            linha = leitura.readLine();
        }

        List<Asteroide> asteroides = this.asteroideService.obterTodos();
        System.out.println("Asteroides carregados:");
        asteroides.forEach(System.out::println);

        leitura.close();
    }
}
