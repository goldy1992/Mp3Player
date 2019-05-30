package com.example.mike.mp3player.daggertest;

import javax.inject.Inject;

public class Main {
    @Inject
    BackendService backendService; //

    private MyComponent component;

    private Main() {
        component = DaggerMyComponent.builder().build();
        component.inject(this);
        component.injectIntoBackendService(backendService);

    }

    private void callServer() {
        boolean callServer = backendService.callServer();
        if (callServer) {
            System.out.println("Server call was successful. ");
        } else {
            System.out.println("Server call failed. ");
        }
    }

    public static void main(String[] args) {
        Main main = new Main();
        main.callServer();
    }
}