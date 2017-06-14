package br.com.softplan.securityservice.service;

import br.com.softplan.securityservice.redis.UserDTO;
import br.com.softplan.securityservice.redis.RedisConnection;
import br.com.softplan.securityservice.redis.UserGateway;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginService {

    public String login(String user, String password) {
        JedisPool connect = RedisConnection.connect();
        Jedis resource = connect.getResource();
        UserGateway gateway = new UserGateway(resource);

        try {
            UserDTO userDTO = gateway.findUser(user);
            if (userDTO.getPassword().equals(password)) {
		        return generateToken(userDTO.getUser(), userDTO.getPassword(), resource);
            }
        } finally {
            if(!connect.isClosed()) {
                connect.close();
            }
        }
        throw new WrongCredentialsException("Wrong credentials");
    }

    public String generateToken(String user, String password, Jedis resource) {
        byte[] tokenGeneratedByte = (user + password + System.currentTimeMillis() / 2).getBytes();
        String tokenGenerated = "";
        try {
            tokenGenerated = MessageDigest.getInstance("MD5").digest(tokenGeneratedByte).toString();
            new UserGateway(resource).updateTokenSession(user, password, tokenGenerated);
            return tokenGenerated;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            resource.close();
        }
    }

    public void register(String email, String user, String password) {
        Jedis resource = RedisConnection.connect().getResource();
        UserGateway gateway = new UserGateway(resource);

        try {
            gateway.register(email, user, password);
        } finally {
            resource.close();
        }
    }

    public boolean validateToken(String user, String token, Jedis resource) {
        UserGateway userGateway = new UserGateway(resource);
        UserDTO userDTO = userGateway.findUser(user);
        String actualToken = userDTO.getTokenActual();

        System.out.println("User: " + user + " - ActualToken: " + actualToken);
        return token.equals(actualToken);
    }
}
