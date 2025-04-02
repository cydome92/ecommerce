package dev.domenicozagaria.ecommerce.unit.repository;

import dev.domenicozagaria.ecommerce.configuration.TestcontainersConfiguration;
import dev.domenicozagaria.ecommerce.dao.entity.ClienteEntity;
import dev.domenicozagaria.ecommerce.dao.entity.OrdineEntity;
import dev.domenicozagaria.ecommerce.dao.entity.OrdineProdottoEntity;
import dev.domenicozagaria.ecommerce.dao.entity.ProdottoEntity;
import dev.domenicozagaria.ecommerce.dao.entity.embedded.OrdineProdottoId;
import dev.domenicozagaria.ecommerce.dao.enumeration.StatoOrdine;
import dev.domenicozagaria.ecommerce.dao.repository.OrdineRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Import(TestcontainersConfiguration.class)
class OrdineRepositoryTest {

    @Autowired
    OrdineRepository repository;
    @Autowired
    TestEntityManager entityManager;

    @Test
    void findAllBy() {
        var cliente = new ClienteEntity();
        cliente.setCodiceFiscale("test");
        cliente.setEmail("test");
        cliente.setDataNascita(LocalDate.now());
        entityManager.persist(cliente);
        insertEntities(cliente);
        var rs = repository.findAllBy(PageRequest.of(0, 10))
                .getContent().getFirst();
        assertNotNull(rs.getCliente());
        assertNotNull(rs.getProdotti());
    }

    @Test
    void findAllByClienteIdIn() {
        var cliente = new ClienteEntity();
        cliente.setCodiceFiscale("test");
        cliente.setEmail("test");
        cliente.setDataNascita(LocalDate.now());
        entityManager.persist(cliente);
        insertEntities(cliente);
        var rs = repository.findAllByClienteIdIn(Set.of(cliente.getId()), PageRequest.of(0, 20))
                .getContent();
        assertFalse(rs.isEmpty());
        assertNotNull(rs.getFirst().getCliente());
        assertNotNull(rs.getFirst().getProdotti());
    }

    private void insertEntities(ClienteEntity cliente) {
        var prodotto = new ProdottoEntity();
        prodotto.setCodice("test");
        prodotto.setNome("test");
        entityManager.persist(prodotto);
        var ordine = new OrdineEntity();
        ordine.setStatoOrdine(StatoOrdine.CONSEGNATO);
        ordine.setCliente(cliente);
        var ordineProdotto = new OrdineProdottoEntity();
        ordineProdotto.setQuantita(1);
        ordineProdotto.setProdotto(prodotto);
        ordineProdotto.setOrdine(ordine);
        ordine.setProdotti(List.of(ordineProdotto));
        entityManager.persist(ordine);
    }

}