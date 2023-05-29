package org.acme.exceptions;

public class ResourceNotFoundException extends Exception{

    public ResourceNotFoundException(String message){
        super(message);
    }
}
