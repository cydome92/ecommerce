package dev.domenicozagaria.ecommerce.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.domenicozagaria.ecommerce.configuration.TestcontainersConfiguration;
import dev.domenicozagaria.ecommerce.dao.dto.ProdottoDTO;
import dev.domenicozagaria.ecommerce.dao.entity.ProdottoEntity;
import dev.domenicozagaria.ecommerce.dao.repository.ProdottoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
public class ProdottoIntegrationTest {

    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper mapper;
    @Autowired
    ProdottoRepository repository;

    final String basePath = "/v1/prodotti";

    @AfterEach
    void deleteDbContent() {
        repository.deleteAll();
    }

    @Test
    void insertProdotto() throws Exception {
        var prodottoExistent = new ProdottoEntity();
        prodottoExistent.setNome("test");
        prodottoExistent.setCodice("test");
        prodottoExistent.setStock(0);
        repository.save(prodottoExistent);
        ProdottoDTO dto1 = new ProdottoDTO(null, "test", "test", 0);
        mvc.perform(post(basePath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto1)))
                .andExpect(status().isConflict());
        ProdottoDTO dto2 = new ProdottoDTO(null, "test2", "test2", 0);
        mvc.perform(post(basePath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto2)))
                .andExpect(status().isCreated());
    }

    @Test
    void searchProdotti() throws Exception {
        var prodottoExistent = new ProdottoEntity();
        prodottoExistent.setNome("test");
        prodottoExistent.setCodice("test");
        prodottoExistent.setStock(0);
        repository.save(prodottoExistent);
        var prodottoExistent2 = new ProdottoEntity();
        prodottoExistent2.setNome("test2");
        prodottoExistent2.setCodice("test2");
        prodottoExistent2.setStock(0);
        repository.save(prodottoExistent2);
        ProdottoDTO dto = new ProdottoDTO(null, "test", "test", 0);
        mvc.perform(post(basePath + "/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto))
                        .queryParam("page", "0")
                        .queryParam("size", "20")
                        .queryParam("sort", "codice,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isNotEmpty());
        //FIXME qualcosa non va qui
    }

}
