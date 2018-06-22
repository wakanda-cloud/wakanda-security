package com.wakanda.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wakanda.security.infrastructure.redis.User;
import com.wakanda.security.infrastructure.redis.UserGateway;
import com.wakanda.security.infrastructure.redis.UserSessionHandler;

@Service
public class LoginService {
	
	@Autowired
	private UserGateway gateway;
	
	@Autowired
	private UserSessionHandler userSessionHandler;

    public String login(String email, String password) {
        User user = gateway.findUser(email);
        if(user == null || !user.getPassword().equals(password)) {
            throw new WrongCredentialsException("Wrong credentials");
        }
        return userSessionHandler.applyToken(user.getEmail(), user.getPassword());
    }

    public void register(String email, String password, String userName, String jobTitle) {
        gateway.register(email, password, userName, jobTitle);
    }
}
