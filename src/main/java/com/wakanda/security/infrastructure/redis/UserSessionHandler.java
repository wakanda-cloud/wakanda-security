package com.wakanda.security.infrastructure.redis;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import redis.clients.jedis.Jedis;

@Component
public class UserSessionHandler {

	@Value("${security.seconds-to-expire-login}")
	private int secondsToExpireLogin;
	
	@Autowired
	private RedisConnection redisConnection;
	
	public String applyToken(String email, String password) {
		byte[] tokenGeneratedByte = (email + password + System.currentTimeMillis() / 2).getBytes();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(tokenGeneratedByte);
			String tokenGenerated = DatatypeConverter.printHexBinary(md.digest()); 
            this.updateTokenSession(email, tokenGenerated);
            return tokenGenerated;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage());
        }
	}
	
	public boolean isValidToken(String email, String token) {
		Jedis resource = redisConnection.connect();
	    UserLogged userLogged = new Gson().fromJson(resource.get(UserLogged.getId(email)), UserLogged.class);
	    if(userLogged == null) {
	    	return false;
	    }
	    
        System.out.println("User: " + email + " - ActualToken: " + token);
        return userLogged.getToken().equals(token);
	}
	
	private void updateTokenSession(String email, String tokenSession) {
    	Jedis resource = redisConnection.connect();
    	
	    log(email, tokenSession);
	    
	    String id = UserLogged.getId(email);
		resource.set(id, new Gson().toJson(new UserLogged(email, tokenSession)));
        resource.expire(id, secondsToExpireLogin);
    }

	private void log(String email, String tokenSession) {
		System.out.println("TokenSessionApplied: " + tokenSession + " for email " + email);
	}
}
