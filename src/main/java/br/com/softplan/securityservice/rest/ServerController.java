package br.com.softplan.securityservice.rest;

import br.com.softplan.securityservice.redis.RedisConnection;
import br.com.softplan.securityservice.redis.UserGateway;
import br.com.softplan.securityservice.service.LoginService;
import com.google.gson.Gson;
import com.wakanda.service.WakandaInstanceData;
import com.wakanda.service.WakandaInstanceHerokuGenerator;
import org.apache.catalina.connector.Response;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@SpringBootApplication
public class ServerController {

    public static void main(String[] args) {
        SpringApplication.run(ServerController.class, args);
    }

    @CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, method = RequestMethod.POST)
    public void login(@RequestParam("user") String user, @RequestParam("password") String password, HttpServletResponse response) throws IOException {
        try {
            String token = new LoginService().login(user, password);
            response.getWriter().write(token);
        } catch (Exception e) {
            responseException(response, e);
        }
    }

    @CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "/registerUser", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, method = RequestMethod.POST)
    public void registerUser(@RequestParam("email") String email, @RequestParam("user") String user, @RequestParam("password") String password) {
        System.out.println("Register user chegou");
        new LoginService().register(email, user, password);
    }

    @CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "/validateToken", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, method = RequestMethod.POST)
    public void validateToken(@RequestParam("user") String user, @RequestParam("token") String token, HttpServletResponse response) throws IOException {
        Jedis resource = RedisConnection.connect().getResource();
        LoginService loginService = new LoginService();
        if(!loginService.validateToken(user, token, resource)) {
            response.setStatus(Response.SC_EXPECTATION_FAILED);
        } else {
            response.setStatus(Response.SC_OK);
        }
    }

    @CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "/generate", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public void generate(@RequestBody WakandaInstanceData data, HttpServletResponse response) throws IOException {
        WakandaInstanceHerokuGenerator generator = new WakandaInstanceHerokuGenerator();
        generator.generateWakandaInstance(data);
    }

    private void responseException(HttpServletResponse response, Exception e) throws IOException {
        response.getWriter().write(e.getMessage());
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

}
