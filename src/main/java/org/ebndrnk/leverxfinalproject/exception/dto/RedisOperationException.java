package org.ebndrnk.leverxfinalproject.exception.dto;

public class RedisOperationException extends RuntimeException{
    public RedisOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
