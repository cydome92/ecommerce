package dev.domenicozagaria.ecommerce.dao.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record ProdottoDTO(
        int id,
        @NotNull String codice,
        @NotNull String nome,
        @PositiveOrZero int quantita
) {
}
