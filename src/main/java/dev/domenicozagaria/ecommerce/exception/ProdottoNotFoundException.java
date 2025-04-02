package dev.domenicozagaria.ecommerce.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "prodotto non trovato")
public class ProdottoNotFoundException extends RuntimeException {

    public ProdottoNotFoundException() {
        super();
    }
}
