package com.example.mike.mp3player.client;

public interface MediaBrowserConnectorCallback {
    void onConnected();
    void onConnectionSuspended();
    void onConnectionFailed();
}
