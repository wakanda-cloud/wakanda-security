package com.wakanda.security.service;

import com.wakanda.security.redis.User;
import com.wakanda.security.redis.RedisConnection;
import com.wakanda.security.redis.UserGateway;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginService {

    public String login(String email, String password) {
        JedisPool connect = RedisConnection.connect();
        Jedis resource = connect.getResource();
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
        Jedis resource = RedisConnection.connect().getResource();
        UserGateway gateway = new UserGateway(resource);

        try {
            gateway.register(email, password, userName, jobTitle);
        } finally {
            resource.close();
        }
    }

    public boolean validateToken(String email, String token, Jedis resource) {
        UserGateway userGateway = new UserGateway(resource);
        User user = userGateway.findUser(email);
        String actualToken = user.getTokenActual();

        System.out.println("User: " + email + " - ActualToken: " + actualToken);
        return token.equals(actualToken);
    }
}
