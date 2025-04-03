package dev.domenicozagaria.ecommerce.unit.controller.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.domenicozagaria.ecommerce.controller.v1.ClienteController;
import dev.domenicozagaria.ecommerce.dao.dto.ClienteDTO;
import dev.domenicozagaria.ecommerce.exception.ClienteExistsException;
import dev.domenicozagaria.ecommerce.service.ClienteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClienteController.class)
class ClienteControllerTest {

    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper mapper;
    @MockitoBean
    ClienteService clienteService;

    final String basePath = "/v1/clienti";

    ClienteDTO dto = new ClienteDTO(null, "nnnccc00a00a000a", "test", "test", LocalDate.now().minusYears(10), "test@test.it", null);

    @Test
    void insertCliente_badRequestWrongBody() throws Exception {
        var wrongDTO = new ClienteDTO(null, "nnnccc00a00a000", "test", "test", LocalDate.now().plusDays(1), "test#test.it", null);
        mvc.perform(post(basePath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(wrongDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void insertCliente_returnsConflict() throws Exception {
        doThrow(ClienteExistsException.class)
                .when(clienteService)
                .insertCliente(any());
        mvc.perform(post(basePath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isConflict());
    }

    @Test
    void insertCliente_returnsCreated() throws Exception {
        when(clienteService.insertCliente(any()))
                .thenReturn(dto);
        mvc.perform(post(basePath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    void searchClienti_noBody() throws Exception {
        when(clienteService.searchCliente(any(), any()))
                .thenReturn(new PagedModel<>(new PageImpl<>(List.of())));
        mvc.perform(post(basePath + "/search")
                        .queryParam("page", "0")
                        .queryParam("size", "20"))
                .andExpect(status().isOk());
    }

    @Test
    void searchClienti_withBody() throws Exception {
        mvc.perform(post(basePath + "/search")
                        .queryParam("page", "0")
                        .queryParam("size", "20")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

}