package dev.domenicozagaria.ecommerce.controller.v1;

import dev.domenicozagaria.ecommerce.dao.dto.ClienteDTO;
import dev.domenicozagaria.ecommerce.dao.dto.OrdineDTO;
import dev.domenicozagaria.ecommerce.dao.enumeration.StatoOrdine;
import dev.domenicozagaria.ecommerce.service.OrdineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
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

    @Operation(
            summary = "Creazione di un nuovo ordine",
            description = """
                    L'unico campo utilizzato all'interno della lista prodotti è il relativo id. Lo stato ordine, come la
                    dataOraCreazione vengono scelti arbitrariamente dal server.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "ordine creato con successo"),
            @ApiResponse(responseCode = "404", description = "cliente o uno o più prodotti non trovati"),
            @ApiResponse(responseCode = "409", description = "quantità ordinata supera lo stock per uno o più prodotti")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrdineDTO insertOrdine(@RequestParam int clienteId, @RequestBody @Valid OrdineDTO body) {
        return ordineService.insertOrdine(clienteId, body);
    }

    @Operation(
            summary = "Ricerca di ordini",
            description = """
                    L'endpoint accetta opzionalmente un body in ingresso di cliente. Se nel body sono avvalorati i campi di nome, cognome
                    e/o codice fiscale, il sistema cercherà utenti con dati simili a database e restituirà tutti i loro ordini
                    come result set paginato. Se il body non è presente, oppure questi campi sono null, il sistema restituirà tutti i risultati
                    in paginazione. Sono valide anche opzioni parziali (solo nome, solo cognome, solo codice fiscale) o combinazione di
                    questi campi. La ricerca è inoltre ignore case. I parametri di ordinamento devono invece fare riferimento all'entità
                    Ordine.
                    """
    )
    @ApiResponse(responseCode = "200", description = "Ricerca avvenuta con successo")
    @PostMapping("search")
    public PagedModel<OrdineDTO> searchOrdini(@RequestBody(required = false) ClienteDTO cliente, Pageable pageable) {
        return ordineService.searchOrdiniByClienteExample(cliente, pageable);
    }

    @Operation(
            summary = "Aggiorna lo stato di un ordine",
            description = """
                    Nel caso in cui venga inserito stato CONSEGNATO, il sistema imposterà automaticamente la dataOraConsegna
                    con fuso orario Roma.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "operazione effettuata con successo"),
            @ApiResponse(responseCode = "404", description = "ordine non trovato")
    })
    @PatchMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateStatoOrdine(@PathVariable int id, @RequestParam StatoOrdine stato) {
        ordineService.updateStatoOrdine(id, stato);
    }

    @Operation(
            summary = "cancellazione di un ordine",
            description = """
                    Se l'ordine risulta in stato CONSEGNATO, la cancellazione andrà in errore. Viceversa, per ogni prodotto
                    inserito nell'ordine, il sistema effettuerà rollback dello stock decrementato in fase di salvataggio
                    rendendo la relativa quantità nuovamente disponibile.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "cancellazione avvenuta con successo"),
            @ApiResponse(responseCode = "404", description = "ordine non trovato"),
            @ApiResponse(responseCode = "409", description = "ordine in stato CONSEGNATO oppure rollback stock in negativo")
    })
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrdine(@PathVariable int id) {
        ordineService.deleteOrdine(id);
    }

}
