package com.prueba_tecnica.API_RESTFul.exception;

public class AttributeBlankOrEmptyException extends RuntimeException {
    public AttributeBlankOrEmptyException(String message) {
        super(message);
    }
}
