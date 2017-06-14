package br.com.softplan.securityservice.redis;

import com.google.gson.Gson;
import redis.clients.jedis.Jedis;

public class UserGateway {

    private Jedis resource;

    public UserGateway(Jedis resource) {
        this.resource = resource;
    }

    public void register(String email, String user, String password) {
        if (resource.exists(user)) {
            throw new UserAlreadyExistsException();
        }

        UserDTO userDTO = new UserDTO();
        userDTO.setUser(user);
        userDTO.setPassword(password);
        userDTO.setEmail(email);
        resource.set("login_" + user, new Gson().toJson(userDTO));
    }

    public UserDTO findUser(String user) {
        UserDTO userDTO = new Gson().fromJson(resource.get("login_" + user), UserDTO.class);
        userDTO.setUser(userDTO.getUser().replace("login_", ""));
        return userDTO;
    }

    public void updateTokenSession(String user, String password, String tokenSession) {
	    UserDTO userDTO = findUser(user);
        userDTO.setTokenActual(tokenSession);
        System.out.println("TokenSessionApplied: " + tokenSession + " for user " + user);
        resource.set("login_" + user, new Gson().toJson(userDTO));
    }
}
