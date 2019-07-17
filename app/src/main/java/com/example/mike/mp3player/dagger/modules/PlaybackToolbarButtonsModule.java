package com.example.mike.mp3player.dagger.modules;

import android.content.Context;

import com.example.mike.mp3player.client.MediaControllerAdapter;
import com.example.mike.mp3player.client.views.PlayPauseButton;
import com.example.mike.mp3player.client.views.SkipToNextButton;
import com.example.mike.mp3player.client.views.SkipToPreviousButton;

import dagger.Module;
import dagger.Provides;

@Module
public class PlaybackToolbarButtonsModule {

    @Provides
    public SkipToPreviousButton provideSkipToPreviousButton(Context context, MediaControllerAdapter mediaControllerAdapter) {
        return new SkipToPreviousButton(context, null, 0, mediaControllerAdapter);
    }

    @Provides
    public SkipToNextButton provideSkipToNextButton(Context context, MediaControllerAdapter mediaControllerAdapter) {
        return new SkipToNextButton(context, null, 0, mediaControllerAdapter);
    }

    @Provides
    public PlayPauseButton providePlayPauseButton(Context context, MediaControllerAdapter mediaControllerAdapter) {
        return new PlayPauseButton(context, null, 0, mediaControllerAdapter);
    }

}
