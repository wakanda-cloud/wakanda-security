package com.wakanda.security.rest;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.connector.Response;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wakanda.security.service.WrongCredentialsException;

@RestController
public class ServerController extends RestServices {

    @CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, method = RequestMethod.POST)
    public void login(@RequestParam("email") String email, @RequestParam("password") String password, HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String token = super.login(email, password);
            response.getWriter().write(token);
            response.setStatus(Response.SC_OK);
        } catch (WrongCredentialsException e) {
            Logger.getLogger("ServerController").log(Level.ALL, "Responding " + Response.SC_UNAUTHORIZED + " for " + email);
            response.setStatus(Response.SC_UNAUTHORIZED);
        } catch(Exception e) {
            response.setStatus(400);
            response.getWriter().write(e.getMessage());
        }
    }

    @CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "/registerUser",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.POST)
    public void registerUser(@RequestBody RegisterData registerData, HttpServletResponse response) throws IOException {
        System.out.println("Cheguei");
        String email = registerData.getEmail();
        String password = registerData.getPassword();
        String user = registerData.getUser();
        String jobTitle = registerData.getJobTitle();
        System.out.println(email + password + user + jobTitle);
        try {
            super.registerUser(email, password, user, jobTitle);
            response.setStatus(200);
        } catch(Exception e) {
            response.setStatus(400);
            response.getWriter().write(e.getMessage());
        }
    }

    @CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "/verifyToken", method = RequestMethod.GET)
    public void verifyToken(@RequestParam("email") String email, @RequestParam("token") String token, HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            System.out.println("Cheguei verify token");
            if (super.validateToken(email, token)) {
                response.setStatus(Response.SC_OK);
            } else {
                response.setStatus(Response.SC_UNAUTHORIZED);
            }
        } catch(Exception e) {
            response.setStatus(Response.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(e.getMessage());
        }
    }
}

class RegisterData {
    private String email;
    private String user;
    private String password;
    private String jobTitle;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }
}