package com.shopio.exception;

public class ReviewNotFoundException extends Throwable {
    public ReviewNotFoundException(String message){
        super(message);
    }
}
