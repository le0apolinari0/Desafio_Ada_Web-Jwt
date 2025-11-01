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
public class PokemonResponse {
    private Long id;
    private Integer idPokeApi;
    private String name;
    private Integer height;
    private Integer weight;
    private String firstAbility;
    private String types;
    private LocalDateTime cachedAt;
    private Boolean favorite;
    private String note;
}

