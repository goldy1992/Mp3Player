package com.example.mike.mp3player.client.callbacks;

import android.media.MediaMetadata;
import android.os.Looper;
import android.support.v4.media.MediaMetadataCompat;
import android.util.Log;

import com.example.mike.mp3player.client.MetaDataListener;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class MyMetaDataCallback extends AsyncCallback<MediaMetadataCompat> {
    private static final String LOG_TAG = "MY_META_DTA_CLLBK";
    private String currentMediaId = null;
    private Set<MetaDataListener> metaDataListeners;


    public MyMetaDataCallback(Looper looper) {
        super(looper);
        this.metaDataListeners = new HashSet<>();
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
                    for (MetaDataListener listener : metaDataListeners) {
                        if (null != listener) {
                            listener.onMetadataChanged(mediaMetadataCompat);
                            sb.append(listener.getClass());
                        }
                    }
                    // Log.i(LOG_TAG, "hit meta data changed " + ", listeners " + metaDataListeners.size() + ", " + sb.toString());
                }
            }
        }
    }

    public synchronized void registerMetaDataListener(MetaDataListener listener) {
        Log.i(LOG_TAG, "registerMetaDataListener" + listener.getClass());
        metaDataListeners.add(listener);
    }

    public synchronized void registerMetaDataListeners(Collection<MetaDataListener> listeners) {
        metaDataListeners.addAll(listeners);
    }

    public synchronized boolean removeMetaDataListener(MetaDataListener listener) {
        return metaDataListeners.remove(listener);
    }
}
