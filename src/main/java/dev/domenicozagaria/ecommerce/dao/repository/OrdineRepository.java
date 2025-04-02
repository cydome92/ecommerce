package dev.domenicozagaria.ecommerce.dao.repository;

import dev.domenicozagaria.ecommerce.dao.entity.OrdineEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface OrdineRepository extends JpaRepository<OrdineEntity, Integer> {

    @EntityGraph(attributePaths = {"cliente", "prodotti", "prodotti.prodotto"})
    Page<OrdineEntity> findAllBy(Pageable pageable);

    @EntityGraph(attributePaths = {"cliente", "prodotti", "prodotti.prodotto"})
    Page<OrdineEntity> findAllByClienteIdIn(Set<Integer> clienteId, Pageable pageable);

}
