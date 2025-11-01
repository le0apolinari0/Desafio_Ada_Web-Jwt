package br.com.ada.adaApi.service;


import br.com.ada.adaApi.dto.request.FavoriteRequest;
import br.com.ada.adaApi.dto.response.PokeApiResponse;
import br.com.ada.adaApi.mapper.PokemonMapper;
import br.com.ada.adaApi.model.Pokemon;
import br.com.ada.adaApi.repository.PokemonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PokemonService {

    private final PokemonRepository pokemonRepository;
    private final PokeApiService pokeApiService;
    private final PokemonMapper pokemonMapper;

    @CacheEvict(value = {"pokemonPage", "pokemonByPokeApiId", "pokemonById"}, allEntries = true)
    public Pokemon cachePokemon(String nameOrId) {
        PokeApiResponse apiResponse = pokeApiService.getPokemon(nameOrId);

        Optional<Pokemon> existingPokemon = pokemonRepository.findByIdPokeApi(apiResponse.getId());

        Pokemon pokemon = existingPokemon.orElse(Pokemon.builder().idPokeApi(apiResponse.getId()).build());

        // Update fields
        pokemon.setName(apiResponse.getName());
        pokemon.setHeight(apiResponse.getHeight());
        pokemon.setWeight(apiResponse.getWeight());

        // Extract first ability
        if (apiResponse.getAbilities() != null && !apiResponse.getAbilities().isEmpty()) {
            pokemon.setFirstAbility(apiResponse.getAbilities().get(0).getAbility().getName());
        }

        // Extract types as CSV
        if (apiResponse.getTypes() != null) {
            String types = apiResponse.getTypes().stream()
                    .map(type -> type.getType().getName())
                    .reduce((a, b) -> a + "," + b)
                    .orElse("");
            pokemon.setTypes(types);
        }

        pokemon.setCachedAt(LocalDateTime.now());

        Pokemon savedPokemon = pokemonRepository.save(pokemon);
        log.info("Pokemon cached successfully: {} (ID: {})", savedPokemon.getName(), savedPokemon.getId());

        return savedPokemon;
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "pokemonPage", key = "#pageable.pageNumber + '-' + #pageable.pageSize")
    public Page<Pokemon> findAll(Pageable pageable) {
        log.debug("Fetching paginated pokemon list - page: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());
        return pokemonRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "pokemonById", key = "#id")
    public Pokemon findById(Long id) {
        log.debug("Fetching pokemon by ID: {}", id);
        return pokemonRepository.findById(id)
                .orElseThrow(() -> new PokemonNotFoundException("Pokemon not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<Pokemon> findByType(String type) {
        log.debug("Searching pokemon by type: {}", type);
        return pokemonRepository.findByTypesContainingIgnoreCase(type);
    }

    @CacheEvict(value = {"pokemonPage", "pokemonById"}, key = "#id")
    public Pokemon updateFavorite(Long id, FavoriteRequest request) {
        log.debug("Updating favorite status for pokemon ID: {}", id);
        Pokemon pokemon = findById(id);
        pokemon.setFavorite(request.getFavorite());
        pokemon.setNote(request.getNote());
        return pokemonRepository.save(pokemon);
    }

    public static class PokemonNotFoundException extends RuntimeException {
        public PokemonNotFoundException(String message) {
            super(message);
        }
    }
}
