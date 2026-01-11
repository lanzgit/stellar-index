package br.edu.infnet.stellarindexapi.model.service;

import br.edu.infnet.stellarindexapi.model.domain.Planeta;
import br.edu.infnet.stellarindexapi.model.domain.exceptions.PlanetaNaoEncotradoException;
import br.edu.infnet.stellarindexapi.model.repository.PlanetaRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PlanetaService implements CrudService<Planeta, Integer> {

  private final PlanetaRepository planetaRepository;
  private final ValidacaoService validacaoService;

  public PlanetaService(PlanetaRepository planetaRepository, ValidacaoService validacaoService) {
    this.planetaRepository = planetaRepository;
    this.validacaoService = validacaoService;
  }

  @Override
  @Transactional
  public Planeta criar(Planeta planeta) {
    this.validacaoService.validarAstro(planeta, planeta.getNome());
    if (planeta.getId() != null && planeta.getId() != 0) {
      throw new IllegalArgumentException("Um novo planeta não pode ter um ID na inclusão");
    }

    if (planeta.getLuas() != null && !planeta.getLuas().isEmpty()) {
      planeta.getLuas().forEach(lua -> lua.setPlaneta(planeta));
    }

    return this.planetaRepository.save(planeta);
  }

  @Override
  @Transactional
  public Planeta obterPorId(Integer id) {
    if (id == null || id <= 0) {
      throw new IllegalArgumentException("o id não pode ser nulo ou zero");
    }
    Planeta planeta =
        this.planetaRepository
            .findById(id)
            .orElseThrow(() -> new PlanetaNaoEncotradoException("O planeta não foi encontrado"));

    return planeta;
  }

  @Override
  @Transactional
  public void excluir(Integer id) {
    Planeta planeta = this.obterPorId(id);
    this.planetaRepository.delete(planeta);
  }

  @Override
  public List<Planeta> obterTodos() {

    return this.planetaRepository.findAll();
  }

  @Override
  @Transactional
  public Planeta atualizar(Planeta planeta, Integer id) {
    if (id == null || id == 0) {
      throw new IllegalArgumentException("O ID não pode ser zero ou nulo");
    }
    this.validacaoService.validarAstro(planeta, planeta.getNome());

    Planeta planetaExistente = this.obterPorId(id);
    planetaExistente.setNome(planeta.getNome());
    planetaExistente.setDescricao(planeta.getDescricao());
    planetaExistente.setTemperaturaMedia(planeta.getTemperaturaMedia());
    planetaExistente.setEhHabitavel(planeta.isEhHabitavel());
    planetaExistente.setGravidade(planeta.getGravidade());
    planetaExistente.setTemSateliteNatural(planeta.isTemSateliteNatural());

    return this.planetaRepository.save(planetaExistente);
  }

  @Transactional
  public Planeta nuke(Integer id) {
    if (id == null || id == 0) {
      throw new IllegalArgumentException("O ID não pode ser zero ou nulo");
    }
    Planeta planeta = this.obterPorId(id);
    if (!planeta.isEhHabitavel()) {
      System.out.println("O planeta " + planeta.getNome() + "já não é mais habitável");
      return planeta;
    }
    planeta.setEhHabitavel(false);

    return this.planetaRepository.save(planeta);
  }
}
