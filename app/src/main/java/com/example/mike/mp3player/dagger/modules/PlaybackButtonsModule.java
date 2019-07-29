package com.example.mike.mp3player.dagger.modules;

import android.content.Context;
import android.os.Handler;

import com.example.mike.mp3player.client.MediaControllerAdapter;
import com.example.mike.mp3player.client.views.PlayPauseButton;
import com.example.mike.mp3player.client.views.RepeatOneRepeatAllButton;
import com.example.mike.mp3player.client.views.ShuffleButton;
import com.example.mike.mp3player.client.views.SkipToNextButton;
import com.example.mike.mp3player.client.views.SkipToPreviousButton;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class PlaybackButtonsModule {

    @Provides
    public SkipToPreviousButton provideSkipToPreviousButton(MediaControllerAdapter mediaControllerAdapter,
                                        @Named("main") Handler mainUpdater) {
        return new SkipToPreviousButton( mediaControllerAdapter, mainUpdater);
    }

    @Provides
    public SkipToNextButton provideSkipToNextButton(MediaControllerAdapter mediaControllerAdapter,
                                                    @Named("main") Handler mainUpdater) {
        return new SkipToNextButton(mediaControllerAdapter, mainUpdater);
    }

    @Provides
    public PlayPauseButton providePlayPauseButton(MediaControllerAdapter mediaControllerAdapter,
                                                  @Named("main") Handler mainUpdater) {
        return new PlayPauseButton(mediaControllerAdapter, mainUpdater);
    }

    @Provides
    public ShuffleButton shuffleButton(MediaControllerAdapter mediaControllerAdapter,
                                       @Named("main") Handler mainUpdater) {
        return new ShuffleButton(mediaControllerAdapter, mainUpdater);
    }

    @Provides
    public RepeatOneRepeatAllButton repeatOneRepeatAllButton(MediaControllerAdapter mediaControllerAdapter,
                                                             @Named("main") Handler mainUpdater) {
        return new RepeatOneRepeatAllButton(mediaControllerAdapter, mainUpdater);
    }

}
