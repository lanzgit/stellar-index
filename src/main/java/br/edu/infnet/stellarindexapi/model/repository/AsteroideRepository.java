package br.edu.infnet.stellarindexapi.model.repository;

import br.edu.infnet.stellarindexapi.model.domain.Asteroide;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AsteroideRepository extends JpaRepository<Asteroide, Integer> {}
