package org.ebndrnk.leverxfinalproject.util.exception.dto;

public class RedisOperationException extends RuntimeException{
    public RedisOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
