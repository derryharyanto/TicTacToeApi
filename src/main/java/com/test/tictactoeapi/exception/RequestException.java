package com.test.tictactoeapi.exception;

public class RequestException extends Exception {
    public RequestException(){}
    public RequestException(String message){
        super(message);
    }
}
