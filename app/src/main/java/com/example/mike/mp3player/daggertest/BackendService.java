package com.example.mike.mp3player.daggertest;

import javax.inject.Inject;
import javax.inject.Named;

public class BackendService {

    @Inject
    public User user;

    private String serverUrl;

    private String anotherUri;

    @Inject
    public BackendService(@Named("serverUrl") String serverUrl, @Named("anotherUri") String anotherUri) {
        this.serverUrl = serverUrl;
        this.anotherUri = anotherUri;
    }

    public boolean callServer() {
        if (user !=null && serverUrl!=null && serverUrl.length()>0) {
            System.out.println("User: " + user + " ServerUrl: "  + serverUrl + " AnotherUri: " + anotherUri);
            return true;
        }
        return false;
    }

}