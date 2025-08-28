package br.edu.infnet.stellarindexapi.model.service;

import br.edu.infnet.stellarindexapi.model.domain.Estrela;
import br.edu.infnet.stellarindexapi.model.domain.exceptions.EstrelaNaoEncontradaException;
import br.edu.infnet.stellarindexapi.model.repository.EstrelaRepository;
import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstrelaService implements CrudService<Estrela, Integer> {

    private final EstrelaRepository estrelaRepository;
    private final ValidacaoService validacaoService;

    public EstrelaService(EstrelaRepository estrelaRepository, ValidacaoService validacaoService) {
        this.estrelaRepository = estrelaRepository;
        this.validacaoService = validacaoService;
    }

    @Override
    public List<Estrela> obterTodos() {
        return this.estrelaRepository.findAll();
    }

    @Override
    @Transactional
    public Estrela obterPorId(Integer id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID inválido");
        }
        Estrela estrela = this.estrelaRepository
            .findById(id)
            .orElseThrow(() -> new EstrelaNaoEncontradaException("Estrela não encontrada"));

        return estrela;
    }

    @Override
    @Transactional
    public Estrela criar(Estrela estrela) {
        this.validacaoService.validarAstro(estrela, estrela.getNome());
        if (estrela.getId() != null && estrela.getId() != 0) {
            throw new IllegalArgumentException("Uma nova estrela não pode ter um ID na inclusão");
        }
        
        return this.estrelaRepository.save(estrela);
    }

    @Override
    @Transactional
    public Estrela atualizar(Estrela estrela, Integer id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID inválido");
        }
        this.validacaoService.validarAstro(estrela, estrela.getNome());

        Estrela estrelaExistente = this.obterPorId(id);
        estrelaExistente.setNome(estrela.getNome());
        estrelaExistente.setDescricao(estrela.getDescricao());
        estrelaExistente.setConstelacao(estrela.getConstelacao());
        estrelaExistente.setEhHabitavel(estrela.isEhHabitavel());
        estrelaExistente.setLuminosidade(estrela.getLuminosidade());
        estrelaExistente.setTemperaturaMedia(estrela.getTemperaturaMedia());

        return this.estrelaRepository.save(estrelaExistente);
    }

    @Override
    @Transactional
    public void excluir(Integer id) {
        Estrela estrela = this.obterPorId(id);
        this.estrelaRepository.delete(estrela);
    }
}
