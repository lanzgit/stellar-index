package br.edu.infnet.stellarindexapi.model.service;

import br.edu.infnet.stellarindexapi.model.domain.exceptions.AstroInvalidoException;
import br.edu.infnet.stellarindexapi.model.domain.exceptions.PlanetaInvalidoException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class ValidacaoService {

    public <T> void validarAstro(T astro, String nome) {
        if (Objects.isNull(astro)) {
            throw new IllegalArgumentException("O astro não pode ser nulo");
        }
        if (nome == null || nome.trim().isEmpty()) {
            throw new AstroInvalidoException("O nome do astro é uma informação obrigatória");
        }
    }
}
