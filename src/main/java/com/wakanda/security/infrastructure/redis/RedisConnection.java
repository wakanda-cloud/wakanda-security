package com.wakanda.security.infrastructure.redis;

import java.net.URI;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

@Component
public class RedisConnection {

	@Value("${redis.url}")
	private String redisUrl;
	
    private Jedis instance;

    public Jedis connect() {
        try {
            if(instance == null) {
                if(System.getenv("REDISCLOUD_URL") != null) {
                	URI redisCloudUri = new URI(System.getenv("REDISCLOUD_URL"));
                	instance = new JedisPool(new JedisPoolConfig(), redisCloudUri.getHost(), redisCloudUri.getPort(), Protocol.DEFAULT_TIMEOUT, redisCloudUri.getUserInfo().split(":", 2)[1]).getResource();
                } else {
                	URI redisSpringConfigurationUri = new URI(redisUrl);
                	instance = new JedisPool(new JedisPoolConfig(), redisSpringConfigurationUri.getHost(), 
                												    redisSpringConfigurationUri.getPort(), Protocol.DEFAULT_TIMEOUT).getResource();
                }
            }
            return instance;
        } catch (Exception e) {
            Logger.getLogger(RedisConnection.class.getName()).warning(e.getMessage());
        }
        return null;
    }
}
