package dev.domenicozagaria.ecommerce.controller.v1;

import dev.domenicozagaria.ecommerce.dao.dto.ClienteDTO;
import dev.domenicozagaria.ecommerce.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/clienti")
public class ClienteController {

    private final ClienteService service;

    public ClienteController(ClienteService service) {
        this.service = service;
    }

    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteDTO insertCliente(@RequestBody @Valid ClienteDTO body) {
        return service.insertCliente(body);
    }

    @PostMapping("search")
    public Page<ClienteDTO> searchClienti(@RequestBody(required = false) ClienteDTO body, Pageable pageable) {
        return service.searchCliente(body, pageable);
    }

}
