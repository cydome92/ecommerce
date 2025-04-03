package dev.domenicozagaria.ecommerce.unit.service;

import dev.domenicozagaria.ecommerce.dao.dto.ClienteDTO;
import dev.domenicozagaria.ecommerce.dao.entity.ClienteEntity;
import dev.domenicozagaria.ecommerce.dao.repository.ClienteRepository;
import dev.domenicozagaria.ecommerce.exception.ClienteExistsException;
import dev.domenicozagaria.ecommerce.exception.ClienteNotFoundException;
import dev.domenicozagaria.ecommerce.service.ClienteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringJUnitConfig(ClienteService.class)
class ClienteServiceTest {

    @Autowired
    ClienteService service;
    @MockitoBean
    ClienteRepository repository;

    @Test
    void insertCliente_codiceFiscaleOrEmailExistsThrowsError() {
        var dto = new ClienteDTO(null, "test", null, null, LocalDate.now(), "test", null);
        when(repository.findFirstByCodiceFiscaleOrEmail(anyString(), anyString()))
                .thenReturn(Optional.of(mock(ClienteEntity.class)));
        assertThrows(ClienteExistsException.class, () -> service.insertCliente(dto));
    }

    @Test
    void insertCliente_success() {
        var dto = new ClienteDTO(null, "test", null, null, LocalDate.now(), "test", null);
        when(repository.findFirstByCodiceFiscaleOrEmail(anyString(), anyString()))
                .thenReturn(Optional.empty());
        when(repository.save(any()))
                .thenReturn(mock(ClienteEntity.class));
        assertDoesNotThrow(() -> service.insertCliente(dto));
    }

    @Test
    void searchCliente_noExampleBody() {
        Pageable pageRequest = PageRequest.of(0, 20);
        when(repository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of()));
        assertDoesNotThrow(() -> service.searchCliente(null, pageRequest));
    }

    @Test
    void searchCliente_withExampleBody() {
        Pageable pageRequest = PageRequest.of(0, 20);
        var exampleBody = new ClienteDTO(null, "test", "test-nome", "test-cognome", null, null, null);
        when(repository.findAll(any(Example.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of()));
        assertDoesNotThrow(() -> service.searchCliente(exampleBody, pageRequest));
    }

    @Test
    void getClienteById_notFound() {
        when(repository.findById(anyInt()))
                .thenReturn(Optional.empty());
        assertThrows(ClienteNotFoundException.class, () -> service.getClienteById(1));
    }
}