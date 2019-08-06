package com.example.mike.mp3player.service.library.db;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Song extends CategoryEntity{

    @NonNull
    @PrimaryKey
    public String uri;

    public String title;

    public String artist;

    public long duration;

    public long albumId;

    @Embedded
    public Folder folder;
}
