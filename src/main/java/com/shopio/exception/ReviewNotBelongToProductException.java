package com.shopio.exception;

public class ReviewNotBelongToProductException extends Throwable {
    public ReviewNotBelongToProductException(String message){
        super(message);
    }
}
