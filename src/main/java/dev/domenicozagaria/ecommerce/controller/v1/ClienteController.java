package dev.domenicozagaria.ecommerce.controller.v1;

import dev.domenicozagaria.ecommerce.dao.dto.ClienteDTO;
import dev.domenicozagaria.ecommerce.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/clienti")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService service;

    @Operation(summary = "Creazione di un nuovo cliente")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "cliente creato con successo"),
            @ApiResponse(responseCode = "409", description = "cliente con stessa email o codice fiscale già censito")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteDTO insertCliente(@RequestBody @Valid ClienteDTO body) {
        return service.insertCliente(body);
    }

    @Operation(
            summary = "Ricerca di clienti",
            description = """
                    L'endpoint accetta opzionalmente un body in ingresso. Se nel body sono avvalorati i campi di nome, cognome
                    e/o codice fiscale, il sistema cercherà utenti con dati simili a database e li restituità come result set
                    paginato. Se il body non è presente, oppure questi campi sono null, il sistema restituirà tutti i risultati
                    in paginazione. Sono valide anche opzioni parziali (solo nome, solo cognome, solo codice fiscale) o combinazione di
                    questi campi. La ricerca è inoltre ignore case.
                    """
    )
    @ApiResponse(responseCode = "200", description = "Ricerca avvenuta con successo")
    @PostMapping("search")
    public PagedModel<ClienteDTO> searchClienti(@RequestBody(required = false) ClienteDTO body, Pageable pageable) {
        return service.searchCliente(body, pageable);
    }

}
