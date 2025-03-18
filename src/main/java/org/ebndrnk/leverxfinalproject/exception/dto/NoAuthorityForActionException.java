package org.ebndrnk.leverxfinalproject.exception.dto;

public class NoAuthorityForActionException extends RuntimeException {
    public NoAuthorityForActionException(String message) {
        super(message);
    }
}
