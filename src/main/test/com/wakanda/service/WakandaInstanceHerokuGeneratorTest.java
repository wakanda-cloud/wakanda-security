package com.wakanda.service;

import org.junit.Test;

import java.io.IOException;

/**
 * Created by ventura on 6/13/17.
 */
public class WakandaInstanceHerokuGeneratorTest {

    @Test
        public void test() throws IOException {
        WakandaInstanceHerokuGenerator generator = new WakandaInstanceHerokuGenerator();
        String name = "wakandatest-app";

        WakandaInstanceData data = new WakandaInstanceData();
        data.setName(name);
        //generator.generateWakandaInstance(data);

        //Runtime.getRuntime().exec("heroku apps:destroy --app " + name);
    }
}
