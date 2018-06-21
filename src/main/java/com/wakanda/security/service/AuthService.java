package com.wakanda.security.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wakanda.security.infrastructure.redis.RedisConnection;

import redis.clients.jedis.Jedis;

@Service
public class AuthService {

	@Autowired
	private RedisConnection redisConnection;
	
	@Autowired
	private LoginService loginService;

    public String login(String email, String password) throws IOException {
        return loginService.login(email, password);
    }

    public void registerUser(String email, String password, String user, String jobTitle) {
        loginService.register(email, password, user, jobTitle);
    }

    public boolean validateToken(String email, String token) {
        System.out.println("Conectarei diretamente na instancia Resource");
        Jedis resource = redisConnection.connect();
        System.out.println("Redis Connected");
        return loginService.validateToken(email, token, resource);
    }
}
