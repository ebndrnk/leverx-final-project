package org.ebndrnk.leverxfinalproject.util.exception.dto;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String message) {
        super(message);
    }
}
