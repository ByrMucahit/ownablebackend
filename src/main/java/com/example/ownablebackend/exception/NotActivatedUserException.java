package com.example.ownablebackend.exception;

public class NotActivatedUserException extends RuntimeException{

    public NotActivatedUserException(String gmail) {
        super(String.format("Hey %s! You have activation mail in your mail box. Please, you may active your profile "
                , gmail));
    }
}
