package br.com.softplan.securityservice.service;

public class WrongCredentialsException extends RuntimeException {

    private String message;

    public WrongCredentialsException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
