package dev.domenicozagaria.ecommerce.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.domenicozagaria.ecommerce.configuration.TestcontainersConfiguration;
import dev.domenicozagaria.ecommerce.dao.dto.ClienteDTO;
import dev.domenicozagaria.ecommerce.dao.dto.OrdineDTO;
import dev.domenicozagaria.ecommerce.dao.dto.ProdottoDTO;
import dev.domenicozagaria.ecommerce.dao.entity.ClienteEntity;
import dev.domenicozagaria.ecommerce.dao.entity.ProdottoEntity;
import dev.domenicozagaria.ecommerce.dao.enumeration.StatoOrdine;
import dev.domenicozagaria.ecommerce.dao.repository.ClienteRepository;
import dev.domenicozagaria.ecommerce.dao.repository.OrdineRepository;
import dev.domenicozagaria.ecommerce.dao.repository.ProdottoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
public class OrdineIntegrationTestIT {

    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper mapper;
    @Autowired
    OrdineRepository repository;
    @Autowired
    ClienteRepository clienteRepository;
    @Autowired
    ProdottoRepository prodottoRepository;

    final String basePath = "/v1/ordini";

    @BeforeEach
    void populateTables() {
        if (clienteRepository.findAll().isEmpty()) {
            var cliente = new ClienteEntity();
            cliente.setDataNascita(LocalDate.now().minusYears(18));
            cliente.setEmail("test@test.it");
            cliente.setCodiceFiscale("NNNCCC00A00A000A");
            cliente.setDataOraIscrizione(LocalDateTime.now().minusMonths(2));
            clienteRepository.save(cliente);
        }
        if (prodottoRepository.findAll().isEmpty()) {
            var prodotto = new ProdottoEntity();
            prodotto.setNome("test");
            prodotto.setCodice("test");
            prodotto.setStock(100);   //qui se la quantità è poca potrebbero andare in errore alcuni integration da un certo punto in poi.
            prodottoRepository.save(prodotto);
        }
    }

    @AfterEach
    void deleteDbContent() {
        repository.deleteAll();
    }

    @Test
    void insertOrdine() throws Exception {
        var cliente = clienteRepository.findAll().getFirst();
        var prodotto = prodottoRepository.findAll().getFirst();
        var prodottoDto = new ProdottoDTO(prodotto.getId(), null, null, 2);
        var clienteDto = new ClienteDTO(cliente.getId(), null, null, null, null, null, null);
        var ordineDto = new OrdineDTO(null, null, clienteDto, List.of(prodottoDto), null);
        mvc.perform(post(basePath)
                        .queryParam("clienteId", Integer.toString(cliente.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(ordineDto)))
                .andExpect(status().isCreated());
        var prodottoDto2 = new ProdottoDTO(prodotto.getId(), null, null, prodotto.getStock() + 1);
        var clienteDto2 = new ClienteDTO(cliente.getId(), null, null, null, null, null, null);
        var ordineDto2 = new OrdineDTO(null, null, clienteDto2, List.of(prodottoDto2), null);
        mvc.perform(post(basePath)
                        .queryParam("clienteId", Integer.toString(cliente.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(ordineDto2)))
                .andExpect(status().isConflict());  //stesso prodotto, ma la quantità è ridotta a 1
    }

    @Test
    void searchOrdini() throws Exception {
        var cliente = clienteRepository.findAll().getFirst();
        var prodotto = prodottoRepository.findAll().getFirst();
        var prodottoDto = new ProdottoDTO(prodotto.getId(), null, null, 2);
        var clienteDto = new ClienteDTO(cliente.getId(), null, null, null, null, null, null);
        var ordineDto = new OrdineDTO(null, null, clienteDto, List.of(prodottoDto), null);
        mvc.perform(post(basePath)
                        .queryParam("clienteId", Integer.toString(cliente.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(ordineDto)))
                .andExpect(status().isCreated());
        var exampleCliente1 = new ClienteDTO(null, null, cliente.getNome(), cliente.getCognome(), null, null, null);
        mvc.perform(post(basePath + "/search")
                        .queryParam("page", "0")
                        .queryParam("size", "20")
                        .queryParam("sort", "dataOraInserimento,desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isNotEmpty());
        mvc.perform(post(basePath + "/search")
                        .queryParam("page", "0")
                        .queryParam("size", "20")
                        .queryParam("sort", "dataOraInserimento,desc")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(exampleCliente1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isNotEmpty());
        var exampleCliente2 = new ClienteDTO(null, null, cliente.getNome() + "-extra", cliente.getCognome() + "-extra", null, null, null);
        mvc.perform(post(basePath + "/search")
                        .queryParam("page", "0")
                        .queryParam("size", "20")
                        .queryParam("sort", "dataOraInserimento,desc")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(exampleCliente2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty());
    }

    @Test
    void updateStatoOrdine() throws Exception {
        var cliente = clienteRepository.findAll().getFirst();
        var prodotto = prodottoRepository.findAll().getFirst();
        var prodottoDto = new ProdottoDTO(prodotto.getId(), null, null, 2);
        var clienteDto = new ClienteDTO(cliente.getId(), null, null, null, null, null, null);
        var ordineDto = new OrdineDTO(null, null, clienteDto, List.of(prodottoDto), null);
        MockHttpServletResponse response = mvc.perform(post(basePath)
                        .queryParam("clienteId", Integer.toString(cliente.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(ordineDto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse();
        OrdineDTO rs = mapper.readValue(response.getContentAsByteArray(), OrdineDTO.class);
        mvc.perform(patch(basePath + "/" + rs.id())
                        .queryParam("stato", StatoOrdine.CONSEGNATO.name()))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteOrdine() throws Exception {
        var cliente = clienteRepository.findAll().getFirst();
        var prodotto = prodottoRepository.findAll().getFirst();
        var prodottoDto = new ProdottoDTO(prodotto.getId(), null, null, 2);
        var clienteDto = new ClienteDTO(cliente.getId(), null, null, null, null, null, null);
        var ordineDto = new OrdineDTO(null, null, clienteDto, List.of(prodottoDto), null);
        MockHttpServletResponse firstInsert = mvc.perform(post(basePath)
                        .queryParam("clienteId", Integer.toString(cliente.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(ordineDto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse();
        OrdineDTO rsFirstInsert = mapper.readValue(firstInsert.getContentAsByteArray(), OrdineDTO.class);
        mvc.perform(delete(basePath + "/" + rsFirstInsert.id()))
                .andExpect(status().isNoContent());
        MockHttpServletResponse secondInsert = mvc.perform(post(basePath)
                        .queryParam("clienteId", Integer.toString(cliente.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(ordineDto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse();
        OrdineDTO rsSecondInsert = mapper.readValue(secondInsert.getContentAsByteArray(), OrdineDTO.class);
        mvc.perform(patch(basePath + "/" + rsSecondInsert.id())
                        .queryParam("stato", StatoOrdine.CONSEGNATO.name()))
                .andExpect(status().isNoContent());
        mvc.perform(delete(basePath + "/" + rsSecondInsert.id()))
                .andExpect(status().isConflict());
    }

}
