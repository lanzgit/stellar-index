package br.edu.infnet.stellarindexapi.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.infnet.stellarindexapi.model.domain.Estrela;

@Repository
public interface EstrelaRepository extends JpaRepository<Estrela, Integer> {

}
