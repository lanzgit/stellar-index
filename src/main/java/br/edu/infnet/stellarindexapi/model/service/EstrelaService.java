package br.edu.infnet.stellarindexapi.model.service;

import br.edu.infnet.stellarindexapi.model.domain.Estrela;
import br.edu.infnet.stellarindexapi.model.domain.exceptions.EstrelaNaoEncontradaException;
import br.edu.infnet.stellarindexapi.model.dto.EstrelaDTO;
import br.edu.infnet.stellarindexapi.model.dto.EstrelaMapper;
import br.edu.infnet.stellarindexapi.model.dto.EstrelaPageDTO;
import br.edu.infnet.stellarindexapi.model.repository.EstrelaRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

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

  public EstrelaPageDTO obterTodosPaginados(int page, int size) {
    Page<Estrela> pageConfig = this.estrelaRepository.findAll(PageRequest.of(page, size));
    List<EstrelaDTO> estrelas =
        pageConfig.get().map(EstrelaMapper::toDTO).collect(Collectors.toList());

    return new EstrelaPageDTO(estrelas, pageConfig.getTotalElements(), pageConfig.getTotalPages());
  }

  @Override
  @Transactional
  public Estrela obterPorId(Integer id) {
    if (id == null || id <= 0) {
      throw new IllegalArgumentException("ID inválido");
    }
    Estrela estrela =
        this.estrelaRepository
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
