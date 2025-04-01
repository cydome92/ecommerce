package dev.domenicozagaria.ecommerce.service;

import dev.domenicozagaria.ecommerce.dao.dto.ProdottoDTO;
import dev.domenicozagaria.ecommerce.dao.repository.ProdottoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProdottoService {

    private final ProdottoRepository repository;

    //TODO tbd
    public ProdottoDTO insertProdotto(ProdottoDTO body) {
        return null;
    }

}
