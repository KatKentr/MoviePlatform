package org.acme.exceptions;

public class MovieNotFoundException extends Exception{

    public MovieNotFoundException(String message){
        super(message);
    }
}
