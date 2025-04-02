package dev.domenicozagaria.ecommerce.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "cliente non trovato")
public class ClienteNotFoundException extends RuntimeException {

    public ClienteNotFoundException() {
        super();
    }
}
