package dev.domenicozagaria.ecommerce.unit.service;

import dev.domenicozagaria.ecommerce.dao.dto.ClienteDTO;
import dev.domenicozagaria.ecommerce.dao.dto.OrdineDTO;
import dev.domenicozagaria.ecommerce.dao.dto.ProdottoDTO;
import dev.domenicozagaria.ecommerce.dao.entity.ClienteEntity;
import dev.domenicozagaria.ecommerce.dao.entity.OrdineEntity;
import dev.domenicozagaria.ecommerce.dao.entity.OrdineProdottoEntity;
import dev.domenicozagaria.ecommerce.dao.entity.ProdottoEntity;
import dev.domenicozagaria.ecommerce.dao.enumeration.StatoOrdine;
import dev.domenicozagaria.ecommerce.dao.repository.OrdineRepository;
import dev.domenicozagaria.ecommerce.exception.OrdineNotFoundException;
import dev.domenicozagaria.ecommerce.exception.OrdineStatoConsegnatoException;
import dev.domenicozagaria.ecommerce.service.ClienteService;
import dev.domenicozagaria.ecommerce.service.OrdineService;
import dev.domenicozagaria.ecommerce.service.ProdottoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringJUnitConfig(OrdineService.class)
class OrdineServiceTest {

    @Autowired
    OrdineService service;
    @MockitoBean
    ClienteService clienteService;
    @MockitoBean
    ProdottoService prodottoService;
    @MockitoBean
    OrdineRepository repository;

    @Test
    void insertOrdine() {
        var cliente = new ClienteEntity(null, "test", null, null, LocalDate.now(), "test@test.it", LocalDateTime.now());
        var prodottoDTO = new ProdottoDTO(1, "test", "test", 2);
        var ordineDTO = new OrdineDTO(
                1,
                null,
                null,
                List.of(prodottoDTO),
                null
        );
        var ordine = new OrdineEntity(
                1,
                null,
                null,
                null,
                cliente,
                List.of()
        );
        when(clienteService.getClienteById(anyInt()))
                .thenReturn(cliente);
        var prodotto = new ProdottoEntity(1, "test", "test", 2);
        when(prodottoService.getProdottiByIds(anySet()))
                .thenReturn(List.of(prodotto));
        when(repository.saveAndFlush(any()))
                .thenReturn(ordine);
        doNothing().when(prodottoService)
                .readdStock(anyMap(), anyList());
        assertDoesNotThrow(() -> service.insertOrdine(1, ordineDTO));
    }

    @Test
    void searchOrdiniByClienteExample_noClienteExampleBody() {
        when(repository.findAllBy(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of()));
        assertDoesNotThrow(() -> service.searchOrdiniByClienteExample(null, PageRequest.of(0, 20)));
    }

    @Test
    void searchOrdiniByClienteExample_withClienteExampleBody() {
        var exampleBody = new ClienteDTO(1, "test", "test", "test", LocalDate.now(), "test@test.it", LocalDateTime.now());
        when(clienteService.searchCliente(any(), any()))
                .thenReturn(new PagedModel<>(new PageImpl<>(List.of(exampleBody))));
        var cliente = new ClienteEntity(1, "test", "test", "test", LocalDate.now(), "test@test.it", LocalDateTime.now());
        var prodotto = new ProdottoEntity(1, "test", "test", 0);
        var ordineProdotto = new OrdineProdottoEntity();
        ordineProdotto.setProdotto(prodotto);
        var rs = new OrdineEntity(1, LocalDateTime.now(), null, StatoOrdine.CONSEGNATO, cliente, List.of(ordineProdotto));
        when(repository.findAllByClienteIdIn(anySet(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(rs)));
        assertDoesNotThrow(() -> service.searchOrdiniByClienteExample(exampleBody, PageRequest.of(0, 20)));
    }

    @Test
    void updateStatoOrdine_ordineNotFound() {
        when(repository.findById(anyInt()))
                .thenReturn(Optional.empty());
        assertThrows(OrdineNotFoundException.class, () -> service.updateStatoOrdine(1, StatoOrdine.CONSEGNATO));
    }

    @Test
    void updateStatoOrdine_success() {
        when(repository.findById(anyInt()))
                .thenReturn(Optional.of(mock(OrdineEntity.class)));
        when(repository.save(any()))
                .thenReturn(mock(OrdineEntity.class));
        assertDoesNotThrow(() -> service.updateStatoOrdine(1, StatoOrdine.CONSEGNATO));
    }

    @Test
    void deleteOrdine_ordineNotFound() {
        when(repository.findById(anyInt()))
                .thenReturn(Optional.empty());
        assertThrows(OrdineNotFoundException.class, () -> service.deleteOrdine(1));
    }

    @Test
    void deleteOrdine_statoConsegnatoThrowsError() {
        var cliente = new ClienteEntity(1, "test", "test", "test", LocalDate.now(), "test@test.it", LocalDateTime.now());
        var rs = new OrdineEntity(1, LocalDateTime.now(), null, StatoOrdine.CONSEGNATO, cliente, List.of());
        when(repository.findById(anyInt()))
                .thenReturn(Optional.of(rs));
        assertThrows(OrdineStatoConsegnatoException.class, () -> service.deleteOrdine(1));
    }

    @Test
    void deleteOrdine_success() {
        var cliente = new ClienteEntity(1, "test", "test", "test", LocalDate.now(), "test@test.it", LocalDateTime.now());
        var rs = new OrdineEntity(1, LocalDateTime.now(), null, StatoOrdine.ORDINATO, cliente, List.of());
        when(repository.findById(anyInt()))
                .thenReturn(Optional.of(rs));
        doNothing().when(repository)
                        .deleteById(anyInt());
        assertDoesNotThrow(() -> service.deleteOrdine(1));
    }

}