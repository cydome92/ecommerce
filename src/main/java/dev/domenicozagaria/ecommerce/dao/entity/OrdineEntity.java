package dev.domenicozagaria.ecommerce.dao.entity;

import dev.domenicozagaria.ecommerce.dao.dto.OrdineDTO;
import dev.domenicozagaria.ecommerce.dao.enumeration.StatoOrdine;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "ordini")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrdineEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @CreationTimestamp(source = SourceType.VM)
    private LocalDateTime dataOraInserimento;
    @Enumerated(EnumType.STRING)
    private StatoOrdine statoOrdine;
    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private ClienteEntity cliente;
    @ManyToMany
    @JoinTable(
            name = "ordini_prodotti",
            joinColumns = {@JoinColumn(name = "id_ordine")},
            inverseJoinColumns = {@JoinColumn(name = "id_prodotto")}
    )
    private List<ProdottoEntity> prodotti;

    public OrdineDTO toDto() {
        return new OrdineDTO(
                id,
                statoOrdine,
                cliente.toDto(),
                prodotti.stream()
                        .map(ProdottoEntity::toDto)
                        .toList(),
                dataOraInserimento
        );
    }

}
