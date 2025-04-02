package dev.domenicozagaria.ecommerce.dao.entity;

import dev.domenicozagaria.ecommerce.dao.entity.embedded.OrdineProdottoId;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ordini_prodotti")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrdineProdottoEntity {

    @EmbeddedId
    private OrdineProdottoId id;

    @ManyToOne
    @MapsId("idOrdine")
    private OrdineEntity ordine;

    @ManyToOne
    @MapsId("idProdotto")
    private ProdottoEntity prodotto;

    private int quantita;



}
