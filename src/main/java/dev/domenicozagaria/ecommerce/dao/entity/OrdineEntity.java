package dev.domenicozagaria.ecommerce.dao.entity;

import dev.domenicozagaria.ecommerce.dao.dto.OrdineDTO;
import dev.domenicozagaria.ecommerce.dao.dto.ProdottoDTO;
import dev.domenicozagaria.ecommerce.dao.enumeration.StatoOrdine;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
    private Integer id;
    @CreationTimestamp(source = SourceType.VM)
    private LocalDateTime dataOraInserimento;
    private LocalDateTime dataOraConsegna;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatoOrdine statoOrdine;
    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private ClienteEntity cliente;
    @OneToMany(mappedBy = "ordine", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<OrdineProdottoEntity> prodotti;

    public OrdineDTO toDto() {
        return new OrdineDTO(
                id,
                statoOrdine,
                cliente.toDto(),
                prodotti.stream()
                        .map(p -> {
                            var prodotto = p.getProdotto();
                            return new ProdottoDTO(
                                    prodotto.getId(),
                                    prodotto.getCodice(),
                                    prodotto.getNome(),
                                    p.getQuantita()
                            );
                        })
                        .toList(),
                dataOraInserimento
        );
    }

}
