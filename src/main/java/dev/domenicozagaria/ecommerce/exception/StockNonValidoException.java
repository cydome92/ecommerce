package dev.domenicozagaria.ecommerce.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "lo stock per un prodotto non può essere inferiore a zero")
public class StockNonValidoException extends RuntimeException {

    public StockNonValidoException() {
        super();
    }
}
