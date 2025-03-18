package org.ebndrnk.leverxfinalproject.exception.dto;

public class UsernameAlreadyExistsException extends RuntimeException{
    public UsernameAlreadyExistsException(String message) {
        super(message);
    }
}
