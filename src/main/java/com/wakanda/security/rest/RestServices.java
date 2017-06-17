package com.wakanda.security.rest;

import com.wakanda.security.redis.RedisConnection;
import com.wakanda.security.service.LoginService;
import redis.clients.jedis.Jedis;

import java.io.IOException;

public class RestServices {

    public String login(String email, String password) throws IOException {
        LoginService loginService = new LoginService();
        return loginService.login(email, password);
    }

    public void registerUser(String email, String password, String user, String jobTitle) {
        LoginService loginService = new LoginService();
        loginService.register(email, password, user, jobTitle);
    }

    public boolean validateToken(String email, String token) {
        Jedis resource = RedisConnection.connect().getResource();
        LoginService loginService = new LoginService();
        return loginService.validateToken(email, token, resource);
    }
}