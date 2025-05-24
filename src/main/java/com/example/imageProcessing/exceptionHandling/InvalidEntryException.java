package com.example.imageProcessing.exceptionHandling;

public class InvalidEntryException extends RuntimeException{

    public InvalidEntryException(String message){
        super(message);
    }
}
