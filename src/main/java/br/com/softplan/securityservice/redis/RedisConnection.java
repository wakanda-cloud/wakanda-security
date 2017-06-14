package br.com.softplan.securityservice.redis;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Logger;

public class RedisConnection {

    private RedisConnection() {
    }

    public static JedisPool connect() {
        try {
            //URI redisUri = new URI(System.getenv("REDISCLOUD_URL"));
            //return new JedisPool(new JedisPoolConfig(), redisUri.getHost(), redisUri.getPort(), Protocol.DEFAULT_TIMEOUT, redisUri.getUserInfo().split(":", 2)[1]);
            return new JedisPool();
        } catch (Exception e) {
            Logger.getLogger(RedisConnection.class.getName()).warning(e.getMessage());
        }
        return null;
    }
}
