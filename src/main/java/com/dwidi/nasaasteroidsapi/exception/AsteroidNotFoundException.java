package com.dwidi.nasaasteroidsapi.exception;

public class AsteroidNotFoundException extends RuntimeException {

    public AsteroidNotFoundException(String message) {
        super(message);
    }
}
