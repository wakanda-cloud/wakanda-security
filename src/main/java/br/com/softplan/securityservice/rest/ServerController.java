package br.com.softplan.securityservice.rest;

import br.com.softplan.securityservice.service.WrongCredentialsException;
import com.wakanda.service.WakandaInstanceData;
import org.apache.catalina.connector.Response;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
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
        }
    }

    @CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "/registerUser",
                    consumes = MediaType.APPLICATION_JSON_VALUE,
                    method = RequestMethod.POST)
    public void registerUser(@RequestBody RegisterData registerData) {
        System.out.println("Cheguei");
        String email = registerData.email;
        String password = registerData.password;
        String user = registerData.user;
        String jobTitle = registerData.jobTitle;
        super.registerUser(email, password, user, jobTitle);
    }

    @CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "/getDashboardData", method = RequestMethod.GET)
    public void getDashboardData(@RequestParam("email") String email, @RequestParam("token") String token, HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(super.validateToken(email, token)) {
            response.setStatus(Response.SC_OK);
            response.getWriter().write("fake dashboard");
        } else {
            response.setStatus(Response.SC_UNAUTHORIZED);
        }
    }

    @CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "/generate", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public void generate(@RequestBody WakandaInstanceData data, HttpServletResponse response) throws IOException {
        super.generate(data);
        response.setStatus(Response.SC_OK);
        response.getWriter().write("Please wait few seconds, we working to turn up your wakanda cloud.");
    }

    private void responseException(HttpServletResponse response, Exception e) throws IOException {
        response.getWriter().write(e.getMessage());
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
}

class RegisterData {
    String email;
    String user;
    String password;
    String jobTitle;
}