package dev.domenicozagaria.ecommerce.unit.service;

import dev.domenicozagaria.ecommerce.dao.dto.ProdottoDTO;
import dev.domenicozagaria.ecommerce.dao.entity.ProdottoEntity;
import dev.domenicozagaria.ecommerce.dao.repository.ProdottoRepository;
import dev.domenicozagaria.ecommerce.exception.CodiceProdottoExistsException;
import dev.domenicozagaria.ecommerce.exception.ProdottoNotFoundException;
import dev.domenicozagaria.ecommerce.exception.QuantitaExceedStockException;
import dev.domenicozagaria.ecommerce.service.ProdottoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringJUnitConfig(ProdottoService.class)
class ProdottoServiceTest {

    @Autowired
    ProdottoService service;
    @MockitoBean
    ProdottoRepository repository;

    @Test
    void insertProdotto_codiceProdottoExistsThrowsError() {
        when(repository.findFirstByCodice(anyString()))
                .thenReturn(Optional.of(mock(ProdottoEntity.class)));
        var body = new ProdottoDTO(null, "test", "test", 0);
        assertThrows(CodiceProdottoExistsException.class, () -> service.insertProdotto(body));
    }

    @Test
    void insertProdotto_success() {
        when(repository.findFirstByCodice(anyString()))
                .thenReturn(Optional.empty());
        var body = new ProdottoDTO(null, "test", "test", 0);
        when(repository.save(any()))
                .thenReturn(new ProdottoEntity(1, "test", "test", 0));
        assertDoesNotThrow(() -> service.insertProdotto(body));
    }

    @Test
    void searchProdotti_noExampleBody() {
        Pageable pageRequest = PageRequest.of(0, 20);
        when(repository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of()));
        assertDoesNotThrow(() -> service.searchProdotti(null, pageRequest));
    }

    @Test
    void searchProdotti_withExampleBody() {
        Pageable pageRequest = PageRequest.of(0, 20);
        var exampleBody = new ProdottoDTO(null, "test", "test", 0);
        when(repository.findAll(any(Example.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of()));
        assertDoesNotThrow(() -> service.searchProdotti(exampleBody, pageRequest));
    }

    @Test
    void getProdottiByIds_isEmptyThrowsError() {
        Set<Integer> ids = Set.of();
        assertThrows(AssertionError.class, () -> service.getProdottiByIds(ids));
    }

    @Test
    void getProdottiByIds_differentSizeThrowsError() {
        Set<Integer> ids = Set.of(1, 2, 3);
        when(repository.findAllById(anySet()))
                .thenReturn(List.of());
        assertThrows(ProdottoNotFoundException.class, () -> service.getProdottiByIds(ids));
    }

    @Test
    void getProdottiByIds_success() {
        Set<Integer> ids = Set.of(1);
        var entity = new ProdottoEntity(1, null, null, 0);
        when(repository.findAllById(anySet()))
                .thenReturn(List.of(entity));
        assertDoesNotThrow(() -> service.getProdottiByIds(ids));
    }

    @Test
    void updateStock_quantitaExceedsStockThrowsError() {
        Map<Integer, Integer> mapIdProdottoQuantita = Map.of(1, 10);
        var prodotto = new ProdottoEntity(1, null, null, 9);
        assertThrows(QuantitaExceedStockException.class, () -> service.updateStock(mapIdProdottoQuantita, List.of(prodotto)));
    }

    @Test
    void updateStock_success() {
        Map<Integer, Integer> mapIdProdottoQuantita = Map.of(1, 10);
        var prodotto = new ProdottoEntity(1, null, null, 10);
        when(repository.saveAllAndFlush(any()))
                .thenReturn(List.of(prodotto));
        assertDoesNotThrow(() -> service.updateStock(mapIdProdottoQuantita, List.of(prodotto)));
    }

}