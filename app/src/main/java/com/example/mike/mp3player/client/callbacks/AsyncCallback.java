package com.example.mike.mp3player.client.callbacks;

import android.os.Handler;
import android.os.Parcelable;

public abstract class AsyncCallback<P extends Parcelable> {
    Handler worker;

    public AsyncCallback(Handler handler) {
        this.worker = handler;
    }

    public final void onStateChanged(P state) {
        worker.post(() -> processCallback(state));
    }

    public abstract void processCallback(P data);
}
