package com.maxkavun.exception;

public class PlayerPersistenceException extends AbstractDaoException {

    public PlayerPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }

    public PlayerPersistenceException(String message) {
        super(message);
    }
}

