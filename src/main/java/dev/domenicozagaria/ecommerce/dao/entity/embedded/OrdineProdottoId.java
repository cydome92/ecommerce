package dev.domenicozagaria.ecommerce.dao.entity.embedded;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class OrdineProdottoId {

    @JoinColumn(name = "id_ordine")
    private Integer idOrdine;
    @JoinColumn(name = "id_prodotto")
    private Integer idProdotto;

}
