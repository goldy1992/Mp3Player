package com.example.mike.mp3player.client.views.buttons;

import android.content.Context;
import android.os.Handler;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.mike.mp3player.client.MediaControllerAdapter;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class MediaButtonTestBase {

    /**
     * mock MediaControllerAdapter
     */
    @Mock
    protected MediaControllerAdapter mediaControllerAdapter;
    /**
     * The main handler used to update the GUI
     */
    @Mock
    protected Handler handler;
    /**
     * Context
     */
    protected Context context;

    /**
     * setup
     */
    protected void setup() {
        MockitoAnnotations.initMocks(this);
        this.context = InstrumentationRegistry.getInstrumentation().getContext();
    }
}
