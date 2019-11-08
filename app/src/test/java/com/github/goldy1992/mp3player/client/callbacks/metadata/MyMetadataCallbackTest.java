package com.github.goldy1992.mp3player.client.callbacks.metadata;

import android.os.Handler;
import android.support.v4.media.MediaMetadataCompat;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static android.support.v4.media.MediaMetadataCompat.METADATA_KEY_MEDIA_ID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@RunWith(RobolectricTestRunner.class)
public class MyMetadataCallbackTest {

    private static MyMetadataCallback myMetadataCallback;

    private static MetadataListener metadataListener = mock(MetadataListener.class);

    @BeforeClass
    public static void setupClass() {
        Handler handler = mock(Handler.class);
        myMetadataCallback = new MyMetadataCallback(handler);
        myMetadataCallback.registerMetaDataListener(metadataListener);
    }

    @Test
    public void testNotifyListenerWithValidMetadata() {
        MediaMetadataCompat mediaMetadataCompat = new MediaMetadataCompat.Builder()
                .putString(METADATA_KEY_MEDIA_ID, "id")
                .build();
        myMetadataCallback.processCallback(mediaMetadataCompat);
        verify(metadataListener, times(1)).onMetadataChanged(mediaMetadataCompat);
    }

    @Test
    public void testNotifyListenerWithNullMetadataCompat() {
        myMetadataCallback.processCallback(null);
        verify(metadataListener, never()).onMetadataChanged(any());
    }

    @AfterClass
    public static void tearDownClass() {
        myMetadataCallback.removeMetaDataListener(metadataListener);
    }


}