package dev.domenicozagaria.ecommerce.controller.v1;

import dev.domenicozagaria.ecommerce.dao.dto.ClienteDTO;
import dev.domenicozagaria.ecommerce.dao.dto.OrdineDTO;
import dev.domenicozagaria.ecommerce.dao.enumeration.StatoOrdine;
import dev.domenicozagaria.ecommerce.service.OrdineService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/ordini")
@RequiredArgsConstructor
public class OrdineController {

    private final OrdineService ordineService;

    @PostMapping("clienti/{clienteId}")
    @ResponseStatus(HttpStatus.CREATED)
    public OrdineDTO insertOrdine(@PathVariable int clienteId, @RequestBody @Valid OrdineDTO body) {
        return ordineService.insertOrdine(clienteId, body);
    }

    @PostMapping("search")
    public Page<OrdineDTO> searchOrdini(@RequestBody(required = false) ClienteDTO cliente, Pageable pageable) {
        return ordineService.searchOrdiniByClienteExample(cliente, pageable);
    }

    @PatchMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateStatoOrdine(@PathVariable int id, @RequestParam StatoOrdine stato) {
        ordineService.updateStatoOrdine(id, stato);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrdine(@PathVariable int id) {
        ordineService.deleteOrdine(id);
    }

}
