package br.edu.infnet.stellarindexapi.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.infnet.stellarindexapi.model.domain.Asteroide;

public interface AsteroideRepository extends JpaRepository<Asteroide, Integer> {
    
}
