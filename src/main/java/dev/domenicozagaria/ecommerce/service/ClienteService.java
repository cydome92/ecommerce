package dev.domenicozagaria.ecommerce.service;

import dev.domenicozagaria.ecommerce.dao.dto.ClienteDTO;
import dev.domenicozagaria.ecommerce.dao.entity.ClienteEntity;
import dev.domenicozagaria.ecommerce.dao.repository.ClienteRepository;
import dev.domenicozagaria.ecommerce.exception.ClientePresentException;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository repository;

    public ClienteDTO insertCliente(ClienteDTO body) {
        repository.findByCodiceFiscaleOrEmail(body.codiceFiscale(), body.email())
                .ifPresent(c -> {
                    throw new ClientePresentException();
                });
        var entity = new ClienteEntity(null, body.codiceFiscale(), body.cognome(), body.dataNascita(), body.email(), LocalDateTime.now());
        return repository.save(entity)
                .toDto();
    }

    public Page<ClienteDTO> searchCliente(@Nullable ClienteDTO body, Pageable pageRequest) {
        Sort sort = pageRequest.getSortOr(Sort.by(Sort.Direction.ASC, "cognome", "codiceFiscale"));
        Pageable pageable = PageRequest.of(pageRequest.getPageNumber(), pageRequest.getPageSize(), sort);
        Page<ClienteDTO> rs;
        if (body == null)
            rs = repository.findAll(pageable)
                    .map(ClienteEntity::toDto);
        else {
            ClienteEntity entity = new ClienteEntity();
            entity.setCodiceFiscale(body.codiceFiscale());
            entity.setCognome(body.cognome());
            ExampleMatcher matcher = ExampleMatcher.matchingAll()
                    .withIgnoreNullValues()
                    .withIgnoreCase(true)
                    .withMatcher("codiceFiscale", ExampleMatcher.GenericPropertyMatchers.contains())
                    .withMatcher("cognome", ExampleMatcher.GenericPropertyMatchers.contains());
            Example<ClienteEntity> example = Example.of(entity, matcher);
            rs = repository.findAll(example, pageable)
                    .map(ClienteEntity::toDto);
        }
        return rs;
    }

}
