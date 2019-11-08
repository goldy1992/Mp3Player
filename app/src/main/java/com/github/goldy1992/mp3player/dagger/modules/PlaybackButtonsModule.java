package com.github.goldy1992.mp3player.dagger.modules;

import android.content.Context;
import android.os.Handler;

import com.github.goldy1992.mp3player.client.MediaControllerAdapter;
import com.github.goldy1992.mp3player.client.views.buttons.PlayPauseButton;
import com.github.goldy1992.mp3player.client.views.buttons.RepeatOneRepeatAllButton;
import com.github.goldy1992.mp3player.client.views.buttons.ShuffleButton;
import com.github.goldy1992.mp3player.client.views.buttons.SkipToNextButton;
import com.github.goldy1992.mp3player.client.views.buttons.SkipToPreviousButton;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class PlaybackButtonsModule {

    @Provides
    public SkipToPreviousButton provideSkipToPreviousButton(Context context, MediaControllerAdapter mediaControllerAdapter,
                                        @Named("main") Handler mainUpdater) {
        return new SkipToPreviousButton(context, mediaControllerAdapter, mainUpdater);
    }

    @Provides
    public SkipToNextButton provideSkipToNextButton(Context context, MediaControllerAdapter mediaControllerAdapter,
                                                    @Named("main") Handler mainUpdater) {
        return new SkipToNextButton(context, mediaControllerAdapter, mainUpdater);
    }

    @Provides
    public PlayPauseButton providePlayPauseButton(Context context, MediaControllerAdapter mediaControllerAdapter,
                                                  @Named("main") Handler mainUpdater) {
        return new PlayPauseButton(context, mediaControllerAdapter, mainUpdater);
    }

    @Provides
    public ShuffleButton shuffleButton(Context context, MediaControllerAdapter mediaControllerAdapter,
                                       @Named("main") Handler mainUpdater) {
        return new ShuffleButton(context, mediaControllerAdapter, mainUpdater);
    }

    @Provides
    public RepeatOneRepeatAllButton repeatOneRepeatAllButton(Context context, MediaControllerAdapter mediaControllerAdapter,
                                                             @Named("main") Handler mainUpdater) {
        return new RepeatOneRepeatAllButton(context, mediaControllerAdapter, mainUpdater);
    }

}
