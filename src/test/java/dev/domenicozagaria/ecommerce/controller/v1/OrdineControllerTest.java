package dev.domenicozagaria.ecommerce.controller.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.domenicozagaria.ecommerce.dao.dto.ClienteDTO;
import dev.domenicozagaria.ecommerce.dao.dto.OrdineDTO;
import dev.domenicozagaria.ecommerce.dao.dto.ProdottoDTO;
import dev.domenicozagaria.ecommerce.dao.enumeration.StatoOrdine;
import dev.domenicozagaria.ecommerce.exception.OrdineNotFoundException;
import dev.domenicozagaria.ecommerce.exception.OrdineStatoConsegnatoException;
import dev.domenicozagaria.ecommerce.exception.QuantitaExceedStockException;
import dev.domenicozagaria.ecommerce.service.OrdineService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrdineController.class)
class OrdineControllerTest {

    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper mapper;
    @MockitoBean
    OrdineService service;

    final String basePath = "/v1/ordini";
    ClienteDTO cliente = new ClienteDTO(
            1,
            "test",
            "test",
            "test",
            LocalDate.now().minusYears(1),
            "test@test.it",
            LocalDateTime.now()
    );

    OrdineDTO ordine = new OrdineDTO(
            null,
            StatoOrdine.CONSEGNATO,
            null,
            List.of(new ProdottoDTO(null, "test", "test", 2)),
            null
    );

    @Test
    void insertOrdine_quantitaExceedsReturnsConflict() throws Exception {
        doThrow(QuantitaExceedStockException.class)
                .when(service)
                .insertOrdine(anyInt(), any());
        mvc.perform(post(basePath)
                        .queryParam("clienteId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(ordine)))
                .andExpect(status().isConflict());
    }

    @Test
    void insertOrdine_created() throws Exception {
        when(service.insertOrdine(anyInt(), any()))
                .thenReturn(ordine);
        mvc.perform(post(basePath)
                        .queryParam("clienteId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(ordine)))
                .andExpect(status().isCreated());
    }

    @Test
    void searchOrdini_noBody() throws Exception {
        when(service.searchOrdiniByClienteExample(any(), any()))
                .thenReturn(new PagedModel<>(new PageImpl<>(List.of(ordine))));
        mvc.perform(post(basePath + "/search")
                        .queryParam("page", "0")
                        .queryParam("size", "20"))
                .andExpect(status().isOk());
    }

    @Test
    void searchOrdini_withBody() throws Exception {
        when(service.searchOrdiniByClienteExample(any(), any()))
                .thenReturn(new PagedModel<>(new PageImpl<>(List.of(ordine))));
        mvc.perform(post(basePath + "/search")
                        .queryParam("page", "0")
                        .queryParam("size", "20")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(cliente)))
                .andExpect(status().isOk());
    }

    @Test
    void updateStatoOrdine_ordineNotFound() throws Exception {
        doThrow(OrdineNotFoundException.class)
                .when(service)
                .updateStatoOrdine(anyInt(), any());
        mvc.perform(patch(basePath + "/1")
                        .queryParam("stato", StatoOrdine.ORDINATO.name()))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateStatoOrdine_success() throws Exception {
        doNothing()
                .when(service)
                .updateStatoOrdine(anyInt(), any());
        mvc.perform(patch(basePath + "/1")
                        .queryParam("stato", StatoOrdine.ORDINATO.name()))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteOrdine_ordineNotFound() throws Exception {
        doThrow(OrdineNotFoundException.class)
                .when(service)
                .deleteOrdine(anyInt());
        mvc.perform(delete(basePath + "/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteOrdine_ordineConsegnatoConflict() throws Exception {
        doThrow(OrdineStatoConsegnatoException.class)
                .when(service)
                .deleteOrdine(anyInt());
        mvc.perform(delete(basePath + "/1"))
                .andExpect(status().isConflict());
    }

    @Test
    void deleteOrdine_success() throws Exception {
        doNothing()
                .when(service)
                .deleteOrdine(anyInt());
        mvc.perform(delete(basePath + "/1"))
                .andExpect(status().isNoContent());
    }

}