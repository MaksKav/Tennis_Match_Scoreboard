package com.maxkavun.exception;

public class AbstractDaoException extends RuntimeException {
    public AbstractDaoException(String message, Throwable cause) {
        super(message, cause);
    }

    public AbstractDaoException(String message) {
        super(message);
    }
}
