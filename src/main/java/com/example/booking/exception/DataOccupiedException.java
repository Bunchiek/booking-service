package com.example.booking.exception;

public class DataOccupiedException extends RuntimeException{
    public DataOccupiedException(String message) {
        super(message);
    }
}
