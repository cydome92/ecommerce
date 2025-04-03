package dev.domenicozagaria.ecommerce.unit.controller.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.domenicozagaria.ecommerce.controller.v1.ProdottoController;
import dev.domenicozagaria.ecommerce.dao.dto.ProdottoDTO;
import dev.domenicozagaria.ecommerce.exception.CodiceProdottoExistsException;
import dev.domenicozagaria.ecommerce.service.ProdottoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProdottoController.class)
class ProdottoControllerTest {

    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper mapper;
    @MockitoBean
    ProdottoService service;

    final String basePath = "/v1/prodotti";
    ProdottoDTO dto = new ProdottoDTO(null, "test", "test", 0);

    @Test
    void insertProdotto_wrongBody() throws Exception {
        var wrongDto = new ProdottoDTO(null, "test", null, 0);
        mvc.perform(post(basePath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(wrongDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void insertProdotto_prodottoExists() throws Exception {
        doThrow(CodiceProdottoExistsException.class)
                .when(service)
                .insertProdotto(any());
        mvc.perform(post(basePath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isConflict());
    }

    @Test
    void insertProdotto_created() throws Exception {
        when(service.insertProdotto(any()))
                .thenReturn(dto);
        mvc.perform(post(basePath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    void searchProdotti_noBody() throws Exception {
        when(service.searchProdotti(any(), any()))
                .thenReturn(new PagedModel<>(new PageImpl<>(List.of())));
        mvc.perform(post(basePath + "/search")
                        .queryParam("page", "0")
                        .queryParam("size", "20"))
                .andExpect(status().isOk());
    }

    @Test
    void searchProdotti_withBody() throws Exception {
        when(service.searchProdotti(any(), any()))
                .thenReturn(new PagedModel<>(new PageImpl<>(List.of())));
        mvc.perform(post(basePath + "/search")
                        .queryParam("page", "0")
                        .queryParam("size", "20")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

}