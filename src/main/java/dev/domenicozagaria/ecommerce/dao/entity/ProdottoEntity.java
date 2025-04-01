package dev.domenicozagaria.ecommerce.dao.entity;

import dev.domenicozagaria.ecommerce.dao.dto.ProdottoDTO;
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

@Entity
@Table(name = "prodotti")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProdottoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(nullable = false, unique = true)
    private String codice;
    @Column(unique = true)
    private String nome;
    private int stock;

    public ProdottoDTO toDto() {
        return new ProdottoDTO(id, codice, nome, stock);
    }

}
