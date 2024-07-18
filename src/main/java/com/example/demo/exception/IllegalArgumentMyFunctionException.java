package com.example.demo.exception;

public class IllegalArgumentMyFunctionException extends RuntimeException{
    public IllegalArgumentMyFunctionException() {
    }

    public IllegalArgumentMyFunctionException(String message) {
        super(message);
    }
}
