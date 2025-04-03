package dev.domenicozagaria.ecommerce.dao.dto;

import dev.domenicozagaria.ecommerce.dao.enumeration.StatoOrdine;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDateTime;
import java.util.List;

public record OrdineDTO(
        Integer id,
        StatoOrdine statoOrdine,
        ClienteDTO cliente,
        @NotEmpty List<ProdottoDTO> prodotti,
        LocalDateTime dataOraInserimento
) {
}
