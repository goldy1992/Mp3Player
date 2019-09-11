package com.example.mike.mp3player.service.library.search;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "songs")
public class Song {

    @PrimaryKey
    @NonNull
    private String id;

    public Song(String id) {
        this.id = id;
    }

    private String title;

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
