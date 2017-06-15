package br.com.softplan.securityservice.redis;

import com.google.gson.Gson;
import redis.clients.jedis.Jedis;

public class UserGateway {

    private Jedis resource;

    public UserGateway(Jedis resource) {
        this.resource = resource;
    }

    public void register(String email, String password, String name, String jobTitle) {
        if (resource.exists(name)) {
            throw new UserAlreadyExistsException();
        }

        User user = new User();
        user.setName(name);
        user.setPassword(password);
        user.setEmail(email);
        user.setJobTitle(jobTitle);
        resource.set(email, new Gson().toJson(user));
    }

    public User findUser(String email) {
        return new Gson().fromJson(resource.get(email), User.class);
    }

    public void updateTokenSession(String email, String password, String tokenSession) {
	    User user = findUser(email);
        user.setTokenActual(tokenSession);
        System.out.println("TokenSessionApplied: " + tokenSession + " for email " + email);
        resource.set(email, new Gson().toJson(user));
    }
}
