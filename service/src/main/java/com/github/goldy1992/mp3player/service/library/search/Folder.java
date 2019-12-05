package com.github.goldy1992.mp3player.service.library.search;

import androidx.room.Entity;

@Entity(tableName = "folders")
public class Folder extends SearchEntity {
    public Folder(String id, String value) {
        super(id, value);
    }
}
