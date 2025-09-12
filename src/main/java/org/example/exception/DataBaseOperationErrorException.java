package org.example.exception;

public class DataBaseOperationErrorException   extends RuntimeException {
 public DataBaseOperationErrorException(String message){
     super(message);
 }
}
