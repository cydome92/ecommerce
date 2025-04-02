package dev.domenicozagaria.ecommerce.controller.v1;

import dev.domenicozagaria.ecommerce.dao.dto.ProdottoDTO;
import dev.domenicozagaria.ecommerce.service.ProdottoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/prodotti")
@RequiredArgsConstructor
public class ProdottoController {

    private final ProdottoService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProdottoDTO insertProdotto(@RequestBody @Valid ProdottoDTO body) {
        return service.insertProdotto(body);
    }

    @PostMapping("search")
    public Page<ProdottoDTO> searchProdotti(@RequestBody(required = false) ProdottoDTO exampleBody, Pageable pageable) {
        return service.searchProdotti(exampleBody, pageable);
    }

}
