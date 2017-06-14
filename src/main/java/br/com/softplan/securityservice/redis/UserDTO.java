package br.com.softplan.securityservice.redis;

public class UserDTO {

    private String user;
    private String password;
    private String email;
    private String tokenActual;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTokenActual() {
        return tokenActual;
    }

    public void setTokenActual(String tokenActual) {
        this.tokenActual = tokenActual;
    }
}
