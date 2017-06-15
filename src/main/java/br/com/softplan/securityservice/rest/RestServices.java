package br.com.softplan.securityservice.rest;

import br.com.softplan.securityservice.redis.RedisConnection;
import br.com.softplan.securityservice.service.LoginService;
import com.wakanda.service.WakandaInstanceData;
import com.wakanda.service.WakandaInstanceHerokuGenerator;
import redis.clients.jedis.Jedis;

import java.io.IOException;

public class RestServices {

    public String login(String email, String password) throws IOException {
        LoginService loginService = new LoginService();
        return loginService.login(email, password);
    }

    public void registerUser(String email, String password, String user, String jobTitle) {
        LoginService loginService = new LoginService();
        loginService.register(email, password, user, jobTitle);
    }

    public boolean validateToken(String email, String token) {
        Jedis resource = RedisConnection.connect().getResource();
        LoginService loginService = new LoginService();
        return loginService.validateToken(email, token, resource);
    }

    public void generate(WakandaInstanceData data) {
        WakandaInstanceHerokuGenerator generator = new WakandaInstanceHerokuGenerator();
        generator.generateWakandaInstance(data);
    }
}
