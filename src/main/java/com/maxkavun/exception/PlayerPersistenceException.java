package com.maxkavun.exception;

public class PlayerPersistenceException extends RuntimeException {

    public PlayerPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }

    public PlayerPersistenceException(String message) {
        super(message);
    }
}

