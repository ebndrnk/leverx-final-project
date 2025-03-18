package org.ebndrnk.leverxfinalproject.exception.dto;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}
