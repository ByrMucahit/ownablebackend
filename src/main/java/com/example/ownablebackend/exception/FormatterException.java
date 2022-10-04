package com.example.ownablebackend.exception;



public class FormatterException extends RuntimeException{


    public FormatterException(String stringExpression) {
        super(String.format("This %s expession is not properly ", stringExpression));
    }
}
