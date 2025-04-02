package dev.domenicozagaria.ecommerce.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "codice prodotto già censito a sistema")
public class CodiceProdottoExistsException extends RuntimeException {

    public CodiceProdottoExistsException() {
        super();
    }
}
