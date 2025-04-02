package dev.domenicozagaria.ecommerce.unit.repository;

import dev.domenicozagaria.ecommerce.configuration.TestcontainersConfiguration;
import dev.domenicozagaria.ecommerce.dao.entity.ProdottoEntity;
import dev.domenicozagaria.ecommerce.dao.repository.ProdottoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(TestcontainersConfiguration.class)
class ProdottoRepositoryTest {

    @Autowired
    ProdottoRepository repository;
    @Autowired
    TestEntityManager entityManager;

    @Test
    void findFirstByCodice() {
        var prodotto = new ProdottoEntity();
        prodotto.setStock(0);
        prodotto.setCodice("test-codice");
        prodotto.setNome("test-nome");
        entityManager.persist(prodotto);
        assertFalse(repository.findFirstByCodice(prodotto.getCodice()).isEmpty());
        assertTrue(repository.findFirstByCodice("test-codice-2").isEmpty());
    }
}