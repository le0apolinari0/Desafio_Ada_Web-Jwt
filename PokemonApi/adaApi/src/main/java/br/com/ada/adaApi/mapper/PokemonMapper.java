package br.com.ada.adaApi.mapper;

import br.com.ada.adaApi.dto.response.PokemonListResponse;
import br.com.ada.adaApi.dto.response.PokemonResponse;
import br.com.ada.adaApi.model.Pokemon;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PokemonMapper {

    PokemonMapper INSTANCE = Mappers.getMapper(PokemonMapper.class);

    PokemonResponse toResponse(Pokemon pokemon);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "idPokeApi", source = "idPokeApi")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "types", source = "types")
    @Mapping(target = "cachedAt", source = "cachedAt")
    PokemonListResponse toListResponse(Pokemon pokemon);

    List<PokemonListResponse> toListResponseList(List<Pokemon> pokemons);
}

