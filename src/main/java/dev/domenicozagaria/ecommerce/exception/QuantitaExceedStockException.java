package dev.domenicozagaria.ecommerce.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "la quantità scelta eccede lo stock per il prodotto")
public class QuantitaExceedStockException extends RuntimeException {

    public QuantitaExceedStockException() {
        super();
    }
}
