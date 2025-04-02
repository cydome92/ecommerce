package dev.domenicozagaria.ecommerce.unit.repository;

import dev.domenicozagaria.ecommerce.configuration.TestcontainersConfiguration;
import dev.domenicozagaria.ecommerce.dao.entity.ClienteEntity;
import dev.domenicozagaria.ecommerce.dao.repository.ClienteRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(TestcontainersConfiguration.class)
class ClienteRepositoryTest {

    @Autowired
    ClienteRepository repository;
    @Autowired
    TestEntityManager entityManager;

    @Test
    void findFirstByCodiceFiscaleOrEmail() {
        var cliente = new ClienteEntity();
        cliente.setCodiceFiscale("test");
        cliente.setEmail("test-email");
        cliente.setDataNascita(LocalDate.now());
        entityManager.persist(cliente);
        assertFalse(repository.findFirstByCodiceFiscaleOrEmail("test", null).isEmpty());
        assertTrue(repository.findFirstByCodiceFiscaleOrEmail("*test-", null).isEmpty());
        assertFalse(repository.findFirstByCodiceFiscaleOrEmail(null, "test-email").isEmpty());
        assertTrue(repository.findFirstByCodiceFiscaleOrEmail(null, "*test-email-").isEmpty());
        assertNotNull(repository.findAll().getFirst().getDataOraIscrizione());
    }
}