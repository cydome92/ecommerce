package dev.domenicozagaria.ecommerce.service;

import dev.domenicozagaria.ecommerce.dao.dto.ProdottoDTO;
import dev.domenicozagaria.ecommerce.dao.entity.ProdottoEntity;
import dev.domenicozagaria.ecommerce.dao.repository.ProdottoRepository;
import dev.domenicozagaria.ecommerce.exception.CodiceProdottoExistsException;
import dev.domenicozagaria.ecommerce.exception.ProdottoNotFoundException;
import dev.domenicozagaria.ecommerce.exception.QuantitaExceedStockException;
import dev.domenicozagaria.ecommerce.service.utils.PaginationUtils;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProdottoService {

    private final ProdottoRepository repository;

    public ProdottoDTO insertProdotto(ProdottoDTO body) {
        repository.findFirstByCodice(body.codice())
                .ifPresent(p -> {
                    throw new CodiceProdottoExistsException();
                });
        ProdottoEntity entity = new ProdottoEntity();
        entity.setCodice(body.codice());
        entity.setNome(body.nome());
        entity.setStock(body.quantita());
        return repository.save(entity)
                .toDto();
    }

    public PagedModel<ProdottoDTO> searchProdotti(@Nullable ProdottoDTO body, Pageable pageRequest) {
        ProdottoEntity entity = new ProdottoEntity();
        Example<ProdottoEntity> example = null;
        if (body != null) {
            entity.setNome(body.nome());
            entity.setCodice(body.codice());
            ExampleMatcher matcher = ExampleMatcher.matchingAll()
                    .withIgnoreNullValues()
                    .withIgnoreCase(true)
                    .withMatcher("nome", ExampleMatcher.GenericPropertyMatchers.contains())
                    .withMatcher("codice", ExampleMatcher.GenericPropertyMatchers.contains());
            example = Example.of(entity, matcher);
        }
        return PaginationUtils.searchFromRepository(example, pageRequest, repository, ProdottoEntity::toDto, Sort.Direction.ASC, "nome", "codice");
    }

    protected List<ProdottoEntity> getProdottiByIds(Set<Integer> ids) {
        assert !ids.isEmpty();
        List<ProdottoEntity> prodotti = repository.findAllById(ids);
        if (ids.size() != prodotti.size())
            throw new ProdottoNotFoundException();
        return prodotti;
    }

    protected void updateStock(Map<Integer, Integer> mapIdProdottoQuantitaScelta, List<ProdottoEntity> prodotti) {
        for (ProdottoEntity p : prodotti) {
            int stock = p.getStock();
            int bought = mapIdProdottoQuantitaScelta.get(p.getId());
            if (bought > stock)
                throw new QuantitaExceedStockException();
            p.setStock(stock - bought);
        }
        repository.saveAllAndFlush(prodotti);
    }

}
