package dev.domenicozagaria.ecommerce.service;

import dev.domenicozagaria.ecommerce.dao.dto.ClienteDTO;
import dev.domenicozagaria.ecommerce.dao.entity.ClienteEntity;
import dev.domenicozagaria.ecommerce.dao.repository.ClienteRepository;
import dev.domenicozagaria.ecommerce.exception.ClienteExistsException;
import dev.domenicozagaria.ecommerce.exception.ClienteNotFoundException;
import dev.domenicozagaria.ecommerce.service.utils.PaginationUtils;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository repository;

    public ClienteDTO insertCliente(ClienteDTO body) {
        repository.findFirstByCodiceFiscaleOrEmail(body.codiceFiscale(), body.email())
                .ifPresent(c -> {
                    throw new ClienteExistsException();
                });
        var entity = new ClienteEntity(null, body.codiceFiscale(), body.nome(), body.cognome(), body.dataNascita(), body.email(), LocalDateTime.now());
        return repository.save(entity)
                .toDto();
    }

    public PagedModel<ClienteDTO> searchCliente(@Nullable ClienteDTO body, Pageable pageRequest) {
        ClienteEntity entity = new ClienteEntity();
        Example<ClienteEntity> example = null;
        if (body != null) {
            entity.setCodiceFiscale(body.codiceFiscale());
            entity.setCognome(body.cognome());
            entity.setNome(body.nome());
            ExampleMatcher matcher = ExampleMatcher.matchingAll()
                    .withIgnoreNullValues()
                    .withIgnoreCase(true)
                    .withMatcher("codiceFiscale", ExampleMatcher.GenericPropertyMatchers.contains())
                    .withMatcher("cognome", ExampleMatcher.GenericPropertyMatchers.contains())
                    .withMatcher("nome", ExampleMatcher.GenericPropertyMatchers.contains());
            example = Example.of(entity, matcher);
        }
        return PaginationUtils.searchFromRepository(example, pageRequest, repository, ClienteEntity::toDto, Sort.Direction.ASC, "cognome", "codiceFiscale");
    }

    protected ClienteEntity getClienteById(int id) {
        return repository.findById(id)
                .orElseThrow(ClienteNotFoundException::new);
    }

}
