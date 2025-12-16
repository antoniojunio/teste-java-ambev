package com.ambev.order.order_service.exception;

public class DuplicateOrderException extends RuntimeException {
    
    public DuplicateOrderException(String message) {
        super(message);
    }
}

