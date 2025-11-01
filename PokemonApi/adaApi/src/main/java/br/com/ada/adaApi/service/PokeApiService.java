package br.com.ada.adaApi.service;

import br.com.ada.adaApi.dto.response.PokeApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;


@Slf4j
@Service
@RequiredArgsConstructor
public class PokeApiService {

    private static final String POKEAPI_BASE_URL = "https://pokeapi.co/api/v2/pokemon/";

    private final RestTemplate restTemplate;

    @Cacheable(value = "pokeApiData", key = "#nameOrId")
    public PokeApiResponse getPokemon(String nameOrId) {
        try {
            log.info("Fetching Pokemon data from PokeAPI for: {}", nameOrId);
            String url = POKEAPI_BASE_URL + nameOrId.toLowerCase();
            return restTemplate.getForObject(url, PokeApiResponse.class);
        } catch (HttpClientErrorException.NotFound e) {
            log.warn("Pokemon not found in PokeAPI: {}", nameOrId);
            throw new PokemonNotFoundException("Pokemon not found in PokeAPI: " + nameOrId);
        } catch (Exception e) {
            log.error("Error fetching Pokemon data from PokeAPI for: {}", nameOrId, e);
            throw new PokeApiIntegrationException("Error fetching data from PokeAPI: " + e.getMessage());
        }
    }

    public static class PokemonNotFoundException extends RuntimeException {
        public PokemonNotFoundException(String message) {
            super(message);
        }
    }

    public static class PokeApiIntegrationException extends RuntimeException {
        public PokeApiIntegrationException(String message) {
            super(message);
        }
    }
}

