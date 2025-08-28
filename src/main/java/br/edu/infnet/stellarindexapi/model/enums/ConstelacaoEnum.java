package br.edu.infnet.stellarindexapi.model.enums;

import lombok.Getter;

@Getter
public enum ConstelacaoEnum {
    
    ARIES("Áries", "Carneiro", "21 de março a 19 de abril"),
    TAURUS("Touro", "Touro", "20 de abril a 20 de maio"),
    GEMINI("Gêmeos", "Gêmeos", "21 de maio a 20 de junho"),
    CANCER("Câncer", "Caranguejo", "21 de junho a 22 de julho"),
    LEO("Leão", "Leão", "23 de julho a 22 de agosto"),
    VIRGO("Virgem", "Virgem", "23 de agosto a 22 de setembro"),
    LIBRA("Libra", "Balança", "23 de setembro a 22 de outubro"),
    SCORPIUS("Escorpião", "Escorpião", "23 de outubro a 21 de novembro"),
    SAGITTARIUS("Sagitário", "Arqueiro", "22 de novembro a 21 de dezembro"),
    CAPRICORNUS("Capricórnio", "Cabra", "22 de dezembro a 19 de janeiro"),
    AQUARIUS("Aquário", "Portador de Água", "20 de janeiro a 18 de fevereiro"),
    PISCES("Peixes", "Peixes", "19 de fevereiro a 20 de março"),
    CYGNUS("Cisne", "Cisne", "22 de dezembro a 19 de janeiro");

    private final String nome;
    private final String simbolo;
    private final String periodo;
    
    ConstelacaoEnum(String nome, String simbolo, String periodo) {
        this.nome = nome;
        this.simbolo = simbolo;
        this.periodo = periodo;
    }
    
    @Override
    public String toString() {
        return String.format("%s (%s) - %s", nome, simbolo, periodo);
    }
}
