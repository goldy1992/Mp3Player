package com.github.goldy1992.mp3player.client;

public interface MediaBrowserConnectorCallback {
    void onConnected();
    void onConnectionSuspended();
    void onConnectionFailed();
}
