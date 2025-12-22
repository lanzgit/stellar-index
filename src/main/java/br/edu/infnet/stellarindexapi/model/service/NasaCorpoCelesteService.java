package br.edu.infnet.stellarindexapi.model.service;

import br.edu.infnet.stellarindexapi.model.clients.NasaSbdbClient;
import br.edu.infnet.stellarindexapi.model.domain.CorpoCeleste;
import br.edu.infnet.stellarindexapi.model.dto.CorpoCelesteDTO;
import br.edu.infnet.stellarindexapi.model.dto.CorpoCelesteMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NasaCorpoCelesteService {
    
    private final NasaSbdbClient nasaSbdbClient;
    private List<String> neosConhecidos = Arrays.asList(
        "433",      // Eros
        "99942",    // Apophis 
        "1620",     // Geographos
        "4179",     // Toutatis
        "25143",    // Itokawa
        "101955",   // Bennu
        "162173",   // Ryugu
        "2063",     // Bacchus
        "1685",     // Toro
        "3753"      // Cruithne
    );
    
    public CorpoCelesteDTO buscarCorpoCeleste(String designacao) {
        try {
            log.info("Buscando informações do corpo celeste: {}", designacao);
            CorpoCeleste corpoCeleste = nasaSbdbClient.buscarCorpoCeleste(designacao);
            
            if (corpoCeleste == null || corpoCeleste.getObject() == null) {
                throw new RuntimeException("Nenhum dado encontrado para: " + designacao);
            }
            log.info("Dados encontrados para: {} - Nome: {}", designacao, corpoCeleste.getNome());

            return CorpoCelesteMapper.toDTO(corpoCeleste);
        } catch (Exception e) {
            log.error("Erro ao buscar corpo celeste {}: {}", designacao, e.getMessage(), e);
            throw new RuntimeException("Erro ao consultar API da NASA para: " + designacao + ". Detalhes: " + e.getMessage(), e);
        }
    }

    public CorpoCeleste buscarCorpoCelesteDetalhado(String designacao) {
        try {
            log.info("Buscando informações detalhadas do corpo celeste: {}", designacao);
            CorpoCeleste corpoCeleste = nasaSbdbClient.buscarCorpoCelesteDetalhado(designacao, true);
            
            if (corpoCeleste == null || corpoCeleste.getObject() == null) {
                throw new RuntimeException("Nenhum dado encontrado para: " + designacao);
            }
            
            log.info("Dados detalhados encontrados para: {} - Nome: {}", designacao, corpoCeleste.getNome());

            return corpoCeleste;
        } catch (Exception e) {
            log.error("Erro ao buscar corpo celeste detalhado {}: {}", designacao, e.getMessage(), e);
            throw new RuntimeException("Erro ao consultar API da NASA para: " + designacao + ". Detalhes: " + e.getMessage(), e);
        }
    }
    
    public boolean ehPotencialmentePerigoso(CorpoCeleste corpoCeleste) {
        if (corpoCeleste == null || corpoCeleste.getObject() == null) {
            return false;
        }
        
        boolean isNEO = corpoCeleste.ehNEO();
        
        String moid = corpoCeleste.getDistanciaMinima();
        boolean distanciaPerigosa = false;
        
        if (moid != null && !moid.isEmpty()) {
            try {
                double moidValue = Double.parseDouble(moid);
                distanciaPerigosa = moidValue < 0.05;
            } catch (NumberFormatException e) {
                log.warn("Não foi possível converter MOID para número: {}", moid);
            }
        }
        
        return isNEO && distanciaPerigosa;
    }
    
    public List<CorpoCelesteDTO> buscarNeosSimplificados() {
        List<CorpoCelesteDTO> neosSimplificados = new ArrayList<>();
        
        for (String designacao : this.neosConhecidos) {
            try {
                log.info("Buscando NEO: {}", designacao);
                CorpoCeleste corpoCeleste = nasaSbdbClient.buscarCorpoCeleste(designacao);
                
                if (corpoCeleste != null && corpoCeleste.getObject() != null && corpoCeleste.ehNEO()) {
                    // Usa o mapper do DTO
                    CorpoCelesteDTO neoDto = CorpoCelesteMapper.toDTO(corpoCeleste);

                    // Validação básica antes de adicionar
                    if (neoDto != null && neoDto.nome() != null && !neoDto.nome().trim().isEmpty()) {
                        neosSimplificados.add(neoDto);
                        log.info("NEO adicionado: {} - Distância: {}", neoDto.nome(), neoDto.distanciaMinima());
                    }
                }
                Thread.sleep(100);
            } catch (Exception e) {
                log.warn("Erro ao buscar NEO {}: {}", designacao, e.getMessage());
            }
        }
        log.info("Total de NEOs encontrados: {}", neosSimplificados.size());

        return neosSimplificados;
    }
}