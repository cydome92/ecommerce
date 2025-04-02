package dev.domenicozagaria.ecommerce.dao.repository;

import dev.domenicozagaria.ecommerce.dao.entity.ProdottoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProdottoRepository extends JpaRepository<ProdottoEntity, Integer> {

    Optional<ProdottoEntity> findFirstByCodice(String codice);

}
