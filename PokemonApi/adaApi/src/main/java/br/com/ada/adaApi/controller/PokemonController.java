package br.com.ada.adaApi.controller;

import br.com.ada.adaApi.dto.request.FavoriteRequest;
import br.com.ada.adaApi.dto.response.PokemonListResponse;
import br.com.ada.adaApi.dto.response.PokemonResponse;
import br.com.ada.adaApi.mapper.PokemonMapper;
import br.com.ada.adaApi.service.PokemonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@RestController
@RequestMapping("/api/pokemon")
@RequiredArgsConstructor
public class PokemonController {

    private final PokemonService pokemonService;
    private final PokemonMapper pokemonMapper;

    @PostMapping("/cache/{nameOrId}")
    public ResponseEntity<PokemonResponse> cachePokemon(@PathVariable String nameOrId) {
        log.info("Received request to cache pokemon: {}", nameOrId);
        var pokemon = pokemonService.cachePokemon(nameOrId);
        var response = pokemonMapper.toResponse(pokemon);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<Page<PokemonListResponse>> listPokemons(Pageable pageable) {
        log.info("Received request to list pokemons - page: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());
        var pokemonPage = pokemonService.findAll(pageable);
        var response = pokemonPage.map(pokemonMapper::toListResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PokemonResponse> getPokemon(@PathVariable Long id) {
        log.info("Received request to get pokemon by ID: {}", id);
        var pokemon = pokemonService.findById(id);
        var response = pokemonMapper.toResponse(pokemon);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<List<PokemonListResponse>> searchByType(@RequestParam String type) {
        log.info("Received request to search pokemon by type: {}", type);
        var pokemons = pokemonService.findByType(type);
        var response = pokemonMapper.toListResponseList(pokemons);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/favorite")
    public ResponseEntity<PokemonResponse> updateFavorite(
            @PathVariable Long id,
            @Valid @RequestBody FavoriteRequest request) {
        log.info("Received request to update favorite for pokemon ID: {}", id);
        var pokemon = pokemonService.updateFavorite(id, request);
        var response = pokemonMapper.toResponse(pokemon);
        return ResponseEntity.ok(response);
    }
}


