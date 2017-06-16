package com.wakanda.service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class WakandaInstanceHerokuGenerator {

    public void generateWakandaInstance(WakandaInstanceData data) {
        List<String> commands = new ArrayList<>();
        try {
            String workFolder = System.getProperty("user.home") + "/saaswakanda";
            commands.add("rm -rf " + workFolder);
            commands.add("mkdir " + workFolder);
            commands.add("git clone http://github.com/lucasventurasc/wakanda.git " + workFolder + "/wakanda");
            commands.add("heroku create <wakandaname>");
            commands.add("heroku config:set DECRYPT_KEY=" + data.getDecryptKey());
            commands.add("heroku config:set SECURITY_KEY=" + data.getSecurityToken());
            commands.add("heroku addons:create heroku-redis:hobby-dev --app <wakandaname>");
            commands.add("heroku git:clone -a <wakandaname> " + workFolder + "/<wakandaname>");
            commands.add("rm -rf " + workFolder + "/wakanda/.git");
            commands.add("cp -R " + workFolder + "/wakanda/. " + workFolder + "/<wakandaname>");
            commands.add("git -C " + workFolder + "/<wakandaname> add .");
            commands.add("git -C " + workFolder + "/<wakandaname> commit -m \"'WakandaService'\" ");
            commands.add("git -C " + workFolder + "/<wakandaname> push heroku master");
            commands.add("rm " + workFolder + " -rf");

            for (String command : commands) {
                command = command.replace("<wakandaname>", data.getName());
                System.out.println(command);
                exec(data, command);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Its not possibile run command line on this machine");
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Its not possibile run command line on this machine 2");
        }
    }

    private void exec(WakandaInstanceData data, String command) throws IOException, InterruptedException {
        Process p = Runtime.getRuntime().exec(command);
        String line;

        BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
        while((line=input.readLine()) != null){
            System.out.println(line);
            WakandaLogger.getInstance(data.getOwnerEmail()).push(line);
        }
        input.close();

        BufferedReader error = new BufferedReader(new InputStreamReader(p.getErrorStream()));
        while((line = error.readLine()) != null){
            System.out.println(line);
            WakandaLogger.getInstance(data.getOwnerEmail()).push(line);
        }
        error.close();
        p.waitFor();
        OutputStream outputStream = p.getOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        printStream.println();
        printStream.flush();
        printStream.close();

    }
}
