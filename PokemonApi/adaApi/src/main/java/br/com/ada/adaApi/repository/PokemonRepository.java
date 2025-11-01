package br.com.ada.adaApi.repository;

import br.com.ada.adaApi.model.Pokemon;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PokemonRepository extends JpaRepository<Pokemon, Long> {

    @Cacheable(value = "pokemonByPokeApiId", key = "#idPokeApi")
    Optional<Pokemon> findByIdPokeApi(Integer idPokeApi);

    @Cacheable(value = "pokemonPage", key = "#pageable.pageNumber + '-' + #pageable.pageSize")
    Page<Pokemon> findAll(Pageable pageable);

    @Query("SELECT p FROM Pokemon p WHERE LOWER(p.types) LIKE LOWER(CONCAT('%', :type, '%'))")
    List<Pokemon> findByTypesContainingIgnoreCase(@Param("type") String type);

    @Cacheable(value = "pokemonById", key = "#id")
    Optional<Pokemon> findById(Long id);

    boolean existsByIdPokeApi(Integer idPokeApi);
}

