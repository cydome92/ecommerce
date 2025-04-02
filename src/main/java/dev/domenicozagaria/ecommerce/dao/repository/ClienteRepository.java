package dev.domenicozagaria.ecommerce.dao.repository;

import dev.domenicozagaria.ecommerce.dao.entity.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<ClienteEntity, Integer> {

    Optional<ClienteEntity> findFirstByCodiceFiscaleOrEmail(String codiceFiscale, String email);

}
