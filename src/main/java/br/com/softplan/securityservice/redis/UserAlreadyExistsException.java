package br.com.softplan.securityservice.redis;

public class UserAlreadyExistsException extends RuntimeException {

    @Override
    public String getMessage() {
        return "User already exists, please choose other";
    }
}
