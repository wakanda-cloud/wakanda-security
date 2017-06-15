package br.com.softplan.securityservice.rest;

import br.com.softplan.securityservice.redis.RedisConnection;
import br.com.softplan.securityservice.service.LoginService;
import com.wakanda.service.WakandaInstanceData;
import com.wakanda.service.WakandaInstanceHerokuGenerator;
import org.apache.catalina.connector.Response;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@RestController
@SpringBootApplication
public class ServerController extends RestServices {

    public static void main(String[] args) {
        SpringApplication.run(ServerController.class, args);
    }

    @CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, method = RequestMethod.POST)
    public void login(@RequestParam("email") String email, @RequestParam("password") String password, HttpServletResponse response) throws IOException {
        try {
            String token = super.login(email, password);
            response.getWriter().write(token);
        } catch (Exception e) {
            responseException(response, e);
        }
    }

    @CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "/registerUser",
                    consumes = MediaType.APPLICATION_JSON_VALUE,
                    method = RequestMethod.POST)
    public void registerUser(@RequestBody Map<String, String> registerData) {
        String email = registerData.get("email");
        String password = registerData.get("password");
        String user = registerData.get("user");
        String jobTitle = registerData.get("jobTitle");
        super.registerUser(email, password, user, jobTitle);
    }

    @CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "/validateToken", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, method = RequestMethod.POST)
    public void validateToken(@RequestParam("email") String email, @RequestParam("token") String token, HttpServletResponse response) throws IOException {
        if(super.validateToken(email, token)) {
            response.setStatus(Response.SC_OK);
        } else {
            response.setStatus(Response.SC_EXPECTATION_FAILED);
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
