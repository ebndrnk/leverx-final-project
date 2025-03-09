package org.ebndrnk.leverxfinalproject.util.exception.dto;

public class UserWithThisEmailNotDoesntChangePasswordException extends RuntimeException{
    public UserWithThisEmailNotDoesntChangePasswordException(String message) {
        super(message);
    }
}
