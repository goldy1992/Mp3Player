package com.github.goldy1992.mp3player.service.library.content;

import android.provider.MediaStore;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class Projections {

    // ensure that an instance of Projections cannot be instantiated
    private Projections() { }

    public static final List<String> SONG_PROJECTION = Collections.unmodifiableList(
            Arrays.asList(
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ALBUM_ID));

    public static final List<String> FOLDER_PROJECTION = Collections.unmodifiableList(
            Arrays.asList(MediaStore.Audio.Media.DATA ));
}
