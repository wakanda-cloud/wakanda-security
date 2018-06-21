package com.wakanda.security.infrastructure.redis;

public class UserAlreadyExistsException extends RuntimeException {

    @Override
    public String getMessage() {
        return "User already exists, please choose other";
    }
}
