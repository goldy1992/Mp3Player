package com.example.mike.mp3player.service.library.content;

import android.provider.MediaStore;

public final class Projections {

    public static final String[] SONG_PROJECTION = {
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ALBUM_ID};

    public static final String[] FOLDER_PROJECTION = { MediaStore.Audio.Media.DATA };
}
