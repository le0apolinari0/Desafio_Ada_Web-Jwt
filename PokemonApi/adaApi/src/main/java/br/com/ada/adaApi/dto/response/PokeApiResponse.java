package br.com.ada.adaApi.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class PokeApiResponse {
    private Integer id;
    private String name;
    private Integer height;
    private Integer weight;
    private List<Ability> abilities;
    private List<Type> types;

    @Data
    public static class Ability {
        @JsonProperty("is_hidden")
        private Boolean isHidden;
        private Integer slot;
        private AbilityDetail ability;
    }

    @Data
    public static class AbilityDetail {
        private String name;
        private String url;
    }

    @Data
    public static class Type {
        private Integer slot;
        private TypeDetail type;
    }

    @Data
    public static class TypeDetail {
        private String name;
        private String url;
    }
}

