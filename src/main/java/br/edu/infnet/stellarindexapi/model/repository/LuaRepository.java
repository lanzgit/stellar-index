package br.edu.infnet.stellarindexapi.model.repository;

import br.edu.infnet.stellarindexapi.model.domain.Lua;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LuaRepository extends JpaRepository<Lua, Integer> {
}
