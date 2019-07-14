package com.example.mike.mp3player.service.player;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.media.session.PlaybackStateCompat;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.mike.mp3player.commons.Constants;
import com.example.mike.mp3player.service.AudioFocusManager;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowMediaPlayer;
import org.robolectric.shadows.util.DataSource;

import static android.support.v4.media.session.PlaybackStateCompat.STATE_PLAYING;
import static org.junit.Assert.*;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class MarshmallowMediaPlayerAdapterTest extends MediaPlayerAdapterTestBase {

    /** {@inheritDoc} */
    @Before
    public void setup() throws IllegalAccessException {
       super.setup();
    }

    @Override
    MarshmallowMediaPlayerAdapter createMediaPlayerAdapter() {
        MarshmallowMediaPlayerAdapter marshmallowMediaPlayerAdapter = new MarshmallowMediaPlayerAdapter(context, audioFocusManager);
        marshmallowMediaPlayerAdapter.setOnCompletionListener(mockOnCompletionListener);
        marshmallowMediaPlayerAdapter.setOnSeekCompleteListener(mockOnSeekCompleteListener);
        return marshmallowMediaPlayerAdapter;
    }
}