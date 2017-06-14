package com.wakanda.service;

public class WakandaInstanceData {

    private String name;
    private String decryptKey;
    private String securityToken;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDecryptKey() {
        return decryptKey;
    }

    public void setDecryptKey(String decryptKey) {
        this.decryptKey = decryptKey;
    }

    public String getSecurityToken() {
        return securityToken;
    }

    public void setSecurityToken(String securityToken) {
        this.securityToken = securityToken;
    }
}
