package br.edu.infnet.stellarindexapi.model.repository;

import br.edu.infnet.stellarindexapi.model.domain.Lua;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LuaRepository extends JpaRepository<Lua, Integer> {

  Optional<List<Lua>> findByPlanetaId(Integer planetaId);

  List<Lua> findByDistanciaOrbitralGreaterThan(Double distancia);
}
