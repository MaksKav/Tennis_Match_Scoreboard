package com.maxkavun.exception;

public class MatchPersistenceException extends AbstractDaoException{
    public MatchPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }

    public MatchPersistenceException(String message) {
        super(message);
    }
}
