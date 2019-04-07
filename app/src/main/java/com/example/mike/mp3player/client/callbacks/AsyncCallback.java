package com.example.mike.mp3player.client.callbacks;

import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;

public abstract class AsyncCallback<P extends Parcelable> {
    Handler worker;

    public AsyncCallback(Looper looper) {
        this.worker = new Handler(looper);
    }

    public final void onStateChanged(P state) {
        worker.post(() -> processCallback(state));
    }

    public abstract void processCallback(P data);
}
