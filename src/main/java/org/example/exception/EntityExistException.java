package org.example.exception;

public class EntityExistException extends RuntimeException{
    public EntityExistException(String message) {
        super(message);
    }
}
