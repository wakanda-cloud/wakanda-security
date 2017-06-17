package com.wakanda.security.redis;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

import java.net.URI;
import java.util.logging.Logger;

public class RedisConnection {

    private RedisConnection() {
    }

    private static JedisPool instance;

    public static JedisPool connect() {
        try {
            if(instance == null) {
                URI redisUri = new URI(System.getenv("REDIS_URL"));
                instance = new JedisPool(new JedisPoolConfig(), redisUri.getHost(), redisUri.getPort(), Protocol.DEFAULT_TIMEOUT, redisUri.getUserInfo().split(":", 2)[1]);
            }
            return instance;
        } catch (Exception e) {
            Logger.getLogger(RedisConnection.class.getName()).warning(e.getMessage());
        }
        return null;
    }
}
