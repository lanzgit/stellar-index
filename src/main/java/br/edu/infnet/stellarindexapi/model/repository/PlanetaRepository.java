package br.edu.infnet.stellarindexapi.model.repository;

import br.edu.infnet.stellarindexapi.model.domain.Planeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanetaRepository extends JpaRepository<Planeta, Integer> {

}
