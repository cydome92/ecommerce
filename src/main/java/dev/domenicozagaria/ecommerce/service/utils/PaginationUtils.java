package dev.domenicozagaria.ecommerce.service.utils;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.function.Function;

public class PaginationUtils {

    public static <T, E, K, R extends JpaRepository<E, K>> Page<T> searchFromRepository(Example<E> exampleBody,
                                                                                        Pageable pageRequest,
                                                                                        R repository,
                                                                                        Function<E, T> toDtoFunction,
                                                                                        Sort.Direction sortDirection,
                                                                                        String... fieldsDefaultSort) {
        Sort sort = pageRequest.getSortOr(Sort.by(sortDirection, fieldsDefaultSort));
        Pageable pageable = PageRequest.of(pageRequest.getPageNumber(), pageRequest.getPageSize(), sort);
        Page<T> rs;
        if (exampleBody == null)
            rs = repository.findAll(pageable)
                    .map(toDtoFunction);
        else {
            rs = repository.findAll(exampleBody, pageable)
                    .map(toDtoFunction);
        }
        return rs;
    }

}
