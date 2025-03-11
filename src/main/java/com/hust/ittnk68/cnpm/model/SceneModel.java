package com.hust.ittnk68.cnpm.model;

public class SceneModel {
    String uriBase;
    String token;

    public void setUriBase(String url) {
        uriBase = url;
    }
    public void setUriBase(String ip, String port) {
        uriBase = String.format("http://%s:%s", ip, port);
    }
    public String getUriBase() {
        return uriBase;
    }

    public String getToken ()
    {
        return token;
    }
    public void setToken (String token)
    {
        this.token = token;
    }
}
