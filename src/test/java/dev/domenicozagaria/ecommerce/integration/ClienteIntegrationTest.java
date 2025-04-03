package dev.domenicozagaria.ecommerce.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.domenicozagaria.ecommerce.configuration.TestcontainersConfiguration;
import dev.domenicozagaria.ecommerce.dao.dto.ClienteDTO;
import dev.domenicozagaria.ecommerce.dao.entity.ClienteEntity;
import dev.domenicozagaria.ecommerce.dao.repository.ClienteRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
public class ClienteIntegrationTest {

    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper mapper;
    @Autowired
    ClienteRepository repository;

    final String basePath = "/v1/clienti";

    @AfterEach
    void deleteDbContent() {
        repository.deleteAll();
    }

    @Test
    void insertCliente_alreadyExists() throws Exception {
        var clienteExistent = new ClienteEntity();
        clienteExistent.setDataNascita(LocalDate.now().minusYears(18));
        clienteExistent.setEmail("test@test.it");
        clienteExistent.setCodiceFiscale("NNNCCC00A00A000A");
        clienteExistent.setDataOraIscrizione(LocalDateTime.now().minusMonths(2));
        repository.save(clienteExistent);

        var body = new ClienteDTO(null, "NNNCCC00A00A000A", null, null, LocalDate.now().minusYears(18), "test@test.it", null);
        mvc.perform(post(basePath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(body)))
                .andExpect(status().isConflict());
    }

    @Test
    void insertCliente_created() throws Exception {
        var clienteExistent = new ClienteEntity();
        clienteExistent.setDataNascita(LocalDate.now().minusYears(18));
        clienteExistent.setEmail("test@test.it");
        clienteExistent.setCodiceFiscale("NNNCCC00A00A000A");
        clienteExistent.setDataOraIscrizione(LocalDateTime.now().minusMonths(2));
        repository.save(clienteExistent);

        var body = new ClienteDTO(null, "NNNCCC00A00A000B", null, null, LocalDate.now().minusYears(18), "test-1@test.it", null);
        mvc.perform(post(basePath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(body)))
                .andExpect(status().isCreated());
    }

    @Test
    void searchCliente() throws Exception {
        var clienteExistent1 = new ClienteEntity();
        clienteExistent1.setDataNascita(LocalDate.now().minusYears(18));
        clienteExistent1.setEmail("test@test.it");
        clienteExistent1.setCodiceFiscale("NNNCCC00A00A000A");
        clienteExistent1.setDataOraIscrizione(LocalDateTime.now().minusMonths(2));
        var clienteExistent2 = new ClienteEntity();
        clienteExistent2.setDataNascita(LocalDate.now().minusYears(18));
        clienteExistent2.setEmail("test1@test.it");
        clienteExistent2.setCodiceFiscale("NNNCCC00A00A000B");
        clienteExistent2.setDataOraIscrizione(LocalDateTime.now().minusMonths(2));
        repository.save(clienteExistent1);
        repository.save(clienteExistent2);
        var body = new ClienteDTO(null, "NNNCCC00A00A000B", null, null, LocalDate.now().minusYears(18), "test-1@test.it", null);
        mvc.perform(post(basePath + "/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(body))
                        .queryParam("page", "0")
                        .queryParam("size", "20")
                        .queryParam("sort", "codiceFiscale,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isNotEmpty());
    }

}
