package com.example.mike.mp3player.service.library.search;

import androidx.room.Entity;

@Entity(tableName = "folders")
public class Folder extends SearchEntity{

    public Folder(String id) {
        super(id);
    }
}
