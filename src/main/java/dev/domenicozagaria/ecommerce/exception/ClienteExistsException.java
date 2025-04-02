package dev.domenicozagaria.ecommerce.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Email o codice fiscale già censito a sistema")
public class ClienteExistsException extends RuntimeException {

    public ClienteExistsException() {
        super();
    }
}
