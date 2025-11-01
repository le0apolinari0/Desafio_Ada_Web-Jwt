package br.com.ada.adaApi.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FavoriteRequest {
    @NotNull(message = "Favorite field is required")
    private Boolean favorite;

    private String note;
}
