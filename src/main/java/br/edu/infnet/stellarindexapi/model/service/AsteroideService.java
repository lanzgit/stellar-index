package br.edu.infnet.stellarindexapi.model.service;

import br.edu.infnet.stellarindexapi.model.clients.NasaSbdbClient;
import br.edu.infnet.stellarindexapi.model.domain.Asteroide;
import br.edu.infnet.stellarindexapi.model.domain.CorpoCeleste;
import br.edu.infnet.stellarindexapi.model.repository.AsteroideRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AsteroideService implements CrudService<Asteroide, Integer> {

  private final NasaSbdbClient nasaSbdbClient;
  private final AsteroideRepository asteroideRepository;
  private final ValidacaoService validacaoService;

  @Override
  public List<Asteroide> obterTodos() {
    return this.asteroideRepository.findAll();
  }

  @Override
  public Asteroide obterPorId(Integer id) {
    if (id == null || id <= 0) {
      throw new IllegalArgumentException("o id não pode ser nulo ou zero");
    }
    return this.asteroideRepository.findById(id).orElse(null);
  }

  @Override
  @Transactional
  public Asteroide criar(Asteroide asteroide) {
    this.validacaoService.validarAstro(asteroide, asteroide.getNome());
    if (asteroide.getId() != null && asteroide.getId() != 0) {
      throw new IllegalArgumentException("Um novo asteroide não pode ter um ID na inclusão");
    }

    CorpoCeleste corpoCeleste = nasaSbdbClient.buscarCorpoCeleste(asteroide.getDesignacao());
    asteroide.copyFromCorpoCelesteResponse(corpoCeleste);

    return this.asteroideRepository.save(asteroide);
  }

  @Override
  @Transactional
  public Asteroide atualizar(Asteroide asteroide, Integer id) {
    if (id == null || id <= 0) {
      throw new IllegalArgumentException("o id não pode ser nulo ou zero");
    }
    this.validacaoService.validarAstro(asteroide, asteroide.getNome());
    Asteroide asteroideExistente = this.obterPorId(id);
    if (asteroideExistente == null) {
      throw new IllegalArgumentException("Asteroide não encontrado");
    }
    asteroideExistente.setNome(asteroide.getNome());
    asteroideExistente.setTemperaturaMedia(asteroide.getTemperaturaMedia());
    asteroideExistente.setDescricao(asteroide.getDescricao());
    asteroideExistente.setEhHabitavel(asteroide.isEhHabitavel());

    if (!asteroide.getDesignacao().equals(asteroideExistente.getDesignacao())) {
      asteroideExistente.setDesignacao(asteroide.getDesignacao());
      CorpoCeleste corpoCeleste = nasaSbdbClient.buscarCorpoCeleste(asteroide.getDesignacao());
      asteroideExistente.copyFromCorpoCelesteResponse(corpoCeleste);
    }

    return this.asteroideRepository.save(asteroideExistente);
  }

  @Override
  @Transactional
  public void excluir(Integer id) {
    if (id == null || id <= 0) {
      throw new IllegalArgumentException("o id não pode ser nulo ou zero");
    }

    Asteroide asteroideExistente = this.asteroideRepository.findById(id).orElse(null);
    if (asteroideExistente == null) {
      throw new IllegalArgumentException("Asteroide não encontrado");
    }

    this.asteroideRepository.delete(asteroideExistente);
  }
}
