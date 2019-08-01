package com.example.mike.mp3player.client.callbacks.metadata;

import android.media.MediaMetadata;
import android.os.Handler;
import android.support.v4.media.MediaMetadataCompat;
import android.util.Log;

import com.example.mike.mp3player.client.callbacks.AsyncCallback;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

public class MyMetadataCallback extends AsyncCallback<MediaMetadataCompat> {
    private static final String LOG_TAG = "MY_META_DTA_CLLBK";
    private String currentMediaId = null;
    private Set<MetadataListener> metadataListeners;

    @Inject
    public MyMetadataCallback(Handler handler) {
        super(handler);
        this.metadataListeners = new HashSet<>();
    }

    @Override
    public void processCallback(MediaMetadataCompat mediaMetadataCompat) {
        if (mediaMetadataCompat != null) {
            MediaMetadata mediaMetadata = (MediaMetadata) mediaMetadataCompat.getMediaMetadata();
            if (null != mediaMetadata && mediaMetadata.getDescription() != null) {
                String newMediaId = mediaMetadata.getDescription().getMediaId();
                if (newMediaId != null && !newMediaId.equals(currentMediaId)) {
                    this.currentMediaId = newMediaId;
                    //logMetaData(mediaMetadataCompat, LOG_TAG);
                    StringBuilder sb = new StringBuilder();
                    for (MetadataListener listener : metadataListeners) {
                        if (null != listener) {
                            listener.onMetadataChanged(mediaMetadataCompat);
                            sb.append(listener.getClass());
                        }
                    }
                    // Log.i(LOG_TAG, "hit meta data changed " + ", listeners " + metadataListeners.size() + ", " + sb.toString());
                }
            }
        }
    }

    public synchronized void registerMetaDataListener(MetadataListener listener) {
        Log.i(LOG_TAG, "registerMetaDataListener" + listener.getClass());
        metadataListeners.add(listener);
    }

    public synchronized void registerMetaDataListeners(Collection<MetadataListener> listeners) {
        metadataListeners.addAll(listeners);
    }

    public synchronized boolean removeMetaDataListener(MetadataListener listener) {
        return metadataListeners.remove(listener);
    }
}
