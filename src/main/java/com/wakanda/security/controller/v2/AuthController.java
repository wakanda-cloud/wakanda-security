package com.wakanda.security.controller.v2;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.wakanda.security.controller.RegisterData;
import com.wakanda.security.service.AuthService;
import com.wakanda.security.service.WrongCredentialsException;

@RestController
@RequestMapping("/v2/auth")
public class AuthController {
	
	@Autowired
	private AuthService authService;

    @CrossOrigin(origins = "*", maxAge = 3600)
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void login(@RequestBody LoginData loginData, HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String token = authService.login(loginData.getEmail(), loginData.getPassword());
            response.getWriter().write(token);
            response.setStatus(Response.SC_OK);
        } catch (WrongCredentialsException e) {
            Logger.getLogger("ServerController").log(Level.ALL, "Responding " + Response.SC_UNAUTHORIZED + " for " + loginData.getEmail());
            response.setStatus(Response.SC_UNAUTHORIZED);
        } catch(Exception e) {
            response.setStatus(400);
            response.getWriter().write(e.getMessage());
        }
    }

    @CrossOrigin(origins = "*", maxAge = 3600)
    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void registerUser(@RequestBody RegisterData registerData, HttpServletResponse response) throws IOException {
        String email = registerData.getEmail();
        String password = registerData.getPassword();
        String user = registerData.getUser();
        String jobTitle = registerData.getJobTitle();
        System.out.println(email + password + user + jobTitle);
        try {
            authService.registerUser(email, password, user, jobTitle);
            response.setStatus(200);
        } catch(Exception e) {
            response.setStatus(400);
            response.getWriter().write(e.getMessage());
        }
    }

    @CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "/verify-token", method = RequestMethod.GET)
    public void verifyToken(@RequestHeader("Auth-Email") String email, @RequestHeader("Auth-Token") String token, HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            System.out.println("Verifying token");
            if (authService.validateToken(email, token)) {
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