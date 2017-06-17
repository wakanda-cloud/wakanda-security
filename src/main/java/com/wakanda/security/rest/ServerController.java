package com.wakanda.security.rest;

import com.wakanda.security.service.WrongCredentialsException;
import org.apache.catalina.connector.Response;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@SpringBootApplication
public class ServerController extends RestServices {

    public static void main(String[] args) {
        SpringApplication.run(ServerController.class, args);
    }

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
    @RequestMapping(value = "/getDashboardData", method = RequestMethod.GET)
    public void getDashboardData(@RequestParam("email") String email, @RequestParam("token") String token, HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (super.validateToken(email, token)) {
            response.setStatus(Response.SC_OK);
            response.getWriter().write("fake dashboard");
        } else {
            response.setStatus(Response.SC_UNAUTHORIZED);
        }
    }

    private void responseException(HttpServletResponse response, Exception e) throws IOException {
        response.getWriter().write(e.getMessage());
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
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