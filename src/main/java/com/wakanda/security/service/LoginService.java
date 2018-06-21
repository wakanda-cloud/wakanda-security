package com.wakanda.security.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wakanda.security.infrastructure.redis.RedisConnection;
import com.wakanda.security.infrastructure.redis.User;
import com.wakanda.security.infrastructure.redis.UserGateway;

import redis.clients.jedis.Jedis;

@Service
public class LoginService {
	
	@Autowired
	private RedisConnection redisConnection;

    public String login(String email, String password) {
        Jedis resource = redisConnection.connect();
        UserGateway gateway = new UserGateway(resource);

        User user = gateway.findUser(email);
        if(user == null || !user.getPassword().equals(password)) {
            throw new WrongCredentialsException("Wrong credentials");
        }
        return generateToken(user.getEmail(), user.getPassword(), resource);
    }

    public String generateToken(String email, String password, Jedis resource) {
        byte[] tokenGeneratedByte = (email + password + System.currentTimeMillis() / 2).getBytes();
        String tokenGenerated = "";
        try {
            tokenGenerated = MessageDigest.getInstance("MD5").digest(tokenGeneratedByte).toString();
            new UserGateway(resource).updateTokenSession(email, password, tokenGenerated);
            return tokenGenerated;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void register(String email, String password, String userName, String jobTitle) {
        Jedis resource = redisConnection.connect();
        UserGateway gateway = new UserGateway(resource);

        gateway.register(email, password, userName, jobTitle);
    }

    public boolean validateToken(String email, String token, Jedis resource) {
        UserGateway userGateway = new UserGateway(resource);
        System.out.println("Buscarei usuario ");
        User user = userGateway.findUser(email);

        if(user == null) {
        	return false;
        }
        
        String actualToken = user.getTokenActual();

        System.out.println("User: " + email + " - ActualToken: " + actualToken);
        return token.equals(actualToken);
    }
}
