package com.wakanda.security.redis;

public class UserAlreadyExistsException extends RuntimeException {

    @Override
    public String getMessage() {
        return "User already exists, please choose other";
    }
}
