package br.com.softplan.securityservice.rest;

import br.com.softplan.securityservice.redis.RedisConnection;
import br.com.softplan.securityservice.service.LoginService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wakanda.service.WakandaInstanceData;
import com.wakanda.service.WakandaInstanceHerokuGenerator;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

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
        JedisPool connect = RedisConnection.connect();
        Jedis resource = connect.getResource();
        String projects = resource.get("projects_" + data.getOwnerEmail());
        if(projects == null || projects == "") {
            addFirstWakandaProjectToThisUser(data, resource);
        } else {
            addNewProjectWakandaForThisUser(data, resource, projects);
        }
    }

    private String getLogsToResponse(List<String> logs) {
        String lines = "";
        if(!logs.isEmpty()) {
            for (String error : logs) {
                lines = lines + "<br>" +error;
            }
        }
        return lines;
    }

    private void addNewProjectWakandaForThisUser(WakandaInstanceData data, Jedis resource, String projects) {
        List<WakandaInstanceData> wakandaProjectsListDeserialized = transformProjectsToArrayList(projects);
        wakandaProjectsListDeserialized.add(data);
        resource.set("projects_" + data.getOwnerEmail(), new Gson().toJson(wakandaProjectsListDeserialized));
    }

    private List<WakandaInstanceData> transformProjectsToArrayList(String projects) {
        Type listType = new TypeToken<ArrayList<WakandaInstanceData>>(){}.getType();
        return new Gson().fromJson(projects, listType);
    }

    private void addFirstWakandaProjectToThisUser(WakandaInstanceData data, Jedis resource) {
        List<WakandaInstanceData> wakandaInstanceDataList = new ArrayList<>();
        wakandaInstanceDataList.add(data);
        resource.set("projects_" + data.getOwnerEmail(), new Gson().toJson(wakandaInstanceDataList));
    }

    public List<WakandaInstanceData> findProjects(String email) {
        Jedis resource = RedisConnection.connect().getResource();
        return transformProjectsToArrayList(resource.get("projects_" + email));
    }
}
