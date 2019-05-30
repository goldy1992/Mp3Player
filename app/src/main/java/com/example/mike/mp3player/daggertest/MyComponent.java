package com.example.mike.mp3player.daggertest;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = { UserModule.class, BackEndServiceModule.class })
public interface MyComponent {

    // provide the dependency for dependent components
    // (not needed for single-component setups)
    BackendService provideBackendService();

    User provideUser();

    // allow to inject into our Main class
    // method name not important
    void inject(Main main);

    void injectIntoBackendService (BackendService service);
}