package org.ebndrnk.leverxfinalproject.exception.dto;

public class UserWithThisEmailNotDoesntChangePasswordException extends RuntimeException{
    public UserWithThisEmailNotDoesntChangePasswordException(String message) {
        super(message);
    }
}
