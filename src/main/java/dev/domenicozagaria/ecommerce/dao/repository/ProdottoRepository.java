package dev.domenicozagaria.ecommerce.dao.repository;

import dev.domenicozagaria.ecommerce.dao.entity.ProdottoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdottoRepository extends JpaRepository<ProdottoEntity, Integer> {
}
