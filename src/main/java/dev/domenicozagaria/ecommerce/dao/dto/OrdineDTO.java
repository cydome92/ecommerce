package dev.domenicozagaria.ecommerce.dao.dto;

import dev.domenicozagaria.ecommerce.dao.enumeration.StatoOrdine;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record OrdineDTO(
        Integer id,
        StatoOrdine statoOrdine,
        @NotNull ClienteDTO cliente,
        @NotEmpty List<ProdottoDTO> prodotti,
        LocalDateTime dataOraInserimento
) {
}
