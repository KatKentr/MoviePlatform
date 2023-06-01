package org.acme.exceptions;

public class DuplicateResourceException extends Exception{

    public DuplicateResourceException(String message) {
        super(message);
    }
}
