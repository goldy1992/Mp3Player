package com.example.mike.mp3player.service.player;

import android.os.Handler;
import android.os.Looper;

import com.google.android.exoplayer2.ControlDispatcher;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.PlaybackParameters;

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

public class SpeedProviderTestBase {

    @Mock
    ExoPlayer exoPlayer;

    Handler handler;

    @Captor
    ArgumentCaptor<PlaybackParameters> captor;

    @Mock
    ControlDispatcher controlDispatcher;

    public void setup() {
        handler = new Handler(Looper.getMainLooper());
    }
}
