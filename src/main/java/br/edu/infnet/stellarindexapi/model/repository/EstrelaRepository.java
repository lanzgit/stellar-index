package br.edu.infnet.stellarindexapi.model.repository;

import br.edu.infnet.stellarindexapi.model.domain.Estrela;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstrelaRepository extends JpaRepository<Estrela, Integer> {}
