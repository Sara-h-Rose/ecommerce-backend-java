package com.sarahrose.ecommerce.exception;

public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException(String email) {
        super("Customer already exists with email: " + email);
    }
}
