package com.wakanda.security.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wakanda.security.infrastructure.redis.UserGateway;

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
    	MessageDigest md = getMD5();
        md.update(password.getBytes());
		String passwordMD5 = DatatypeConverter.printHexBinary(md.digest()); 
    	
        gateway.register(email, passwordMD5, userName, jobTitle);
    }

	private MessageDigest getMD5() {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		return md;
	}
}
