package br.com.ada.adaApi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PokemonListResponse {
    private Long id;
    private Integer idPokeApi;
    private String name;
    private String types;
    private LocalDateTime cachedAt;
}

