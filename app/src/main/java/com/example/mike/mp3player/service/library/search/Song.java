package com.example.mike.mp3player.service.library.search;

import androidx.annotation.NonNull;
import androidx.room.Entity;

@Entity(tableName = "songs")
public class Song extends SearchEntity {

    public Song(String id) {
        super(id);
    }
}
