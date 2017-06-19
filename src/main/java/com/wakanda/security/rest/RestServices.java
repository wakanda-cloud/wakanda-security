package com.wakanda.security.rest;

import com.wakanda.security.redis.RedisConnection;
import com.wakanda.security.service.LoginService;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

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
        JedisPool connect = RedisConnection.connect();
        System.out.println("Peguei a instancia e tentarei conectar");
        Jedis resource = connect.getResource();
        System.out.println("Conectei no redis");
        LoginService loginService = new LoginService();
        return loginService.validateToken(email, token, resource);
    }
}