package com.wakanda.security.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wakanda.security.infrastructure.redis.UserSessionHandler;

@Service
public class AuthService {

	@Autowired
	private LoginService loginService;
	
	@Autowired
	private UserSessionHandler userSessionHandler;

    public String login(String email, String password) throws IOException {
        return loginService.login(email, password);
    }

    public void registerUser(String email, String password, String user, String jobTitle) {
        loginService.register(email, password, user, jobTitle);
    }

    public boolean validateToken(String email, String token) {
        return userSessionHandler.isValidToken(email, token);
    }
}
