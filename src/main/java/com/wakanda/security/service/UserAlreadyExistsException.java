package com.wakanda.security.service;

public class UserAlreadyExistsException extends RuntimeException {

    @Override
    public String getMessage() {
        return "User already exists, please choose other";
    }
}
