package com.wakanda.security.infrastructure.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import redis.clients.jedis.Jedis;

@Component
public class UserGateway {

	@Autowired
	private RedisConnection redisConnection;
	
    public void register(String email, String password, String name, String jobTitle) {
        Jedis resource = redisConnection.connect();
		if (resource.exists(name)) {
            throw new UserAlreadyExistsException();
        }

        User user = new User();
        user.setName(name);
        user.setPassword(password);
        user.setEmail(email);
        user.setJobTitle(jobTitle);
        resource.set(email, new Gson().toJson(user));
    }

    public User findUser(String email) {
        return new Gson().fromJson(redisConnection.connect().get(email), User.class);
    }
}
