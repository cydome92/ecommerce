package dev.domenicozagaria.ecommerce.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "ordine in stato consegnato")
public class OrdineStatoConsegnatoException extends RuntimeException {

    public OrdineStatoConsegnatoException() {
        super();
    }
}
