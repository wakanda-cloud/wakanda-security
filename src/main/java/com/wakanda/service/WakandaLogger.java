package com.wakanda.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ventura on 6/16/17.
 */
public class WakandaLogger {

    private static Map<String, WakandaLogger> logsByEmail;

    private static final Integer OPERATION_PUSH = 0;
    private static final Integer OPERATION_PULL = 1;

    private List<String> logs;

    private WakandaLogger() {
        logs = new ArrayList<String>();
    }

    public static WakandaLogger getInstance(String email) {
        if(logsByEmail == null) {
            logsByEmail = new HashMap<>();
        }
        if(!logsByEmail.containsKey(email)) {
            logsByEmail.put(email, new WakandaLogger());
        }
        return logsByEmail.get(email);
    }

    public void push(String log) {
        manipulateLog(log, OPERATION_PUSH);
    }

    public List<String> pull() {
       return manipulateLog(null, OPERATION_PULL);
    }

    private synchronized List<String> manipulateLog(String log, Integer operation) {
        if(operation==0) {
            logs.add(log);
        } else if(operation == 1) {
            List<String> copiedLogs = new ArrayList<String>();
            for (String logStr : logs) {
                copiedLogs.add(logStr);
            }
            logs.clear();
            return copiedLogs;
        }
        return null;
    }
}
