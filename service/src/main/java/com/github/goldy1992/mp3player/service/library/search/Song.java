package com.github.goldy1992.mp3player.service.library.search;

import androidx.room.Entity;

@Entity(tableName = "songs")
public class Song extends SearchEntity {

    public Song(String id, String value) {
        super(id, value);
    }
}
