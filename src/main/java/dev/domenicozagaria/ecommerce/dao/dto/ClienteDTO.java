package dev.domenicozagaria.ecommerce.dao.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record ClienteDTO(
        int id,
        @Size(min = 16, max = 16) String codiceFiscale,
        String cognome,
        @Past LocalDate dataNascita,
        @Email String email
) {
}
