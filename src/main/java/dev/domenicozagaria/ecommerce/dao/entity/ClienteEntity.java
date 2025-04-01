package dev.domenicozagaria.ecommerce.dao.entity;

import dev.domenicozagaria.ecommerce.dao.dto.ClienteDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "clienti")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ClienteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(nullable = false, unique = true)
    private String codiceFiscale;
    private String cognome;
    @Column(nullable = false)
    private LocalDate dataNascita;
    @Column(nullable = false, unique = true)
    private String email;

    public ClienteDTO toDto() {
        return new ClienteDTO(id, codiceFiscale, cognome, dataNascita, email);
    }

}
