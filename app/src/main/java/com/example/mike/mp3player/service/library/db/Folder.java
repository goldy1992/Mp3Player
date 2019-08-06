package com.example.mike.mp3player.service.library.db;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.mike.mp3player.commons.library.Category;

@Entity
public class Folder extends CategoryEntity {

    @PrimaryKey
    @NonNull
    public String path;


    public String name;

    @Override
    public boolean equals(Object object) {
        if (object instanceof Folder) {
            Folder folder = (Folder) object;
            return this.path.equals(folder.path);
        }
        return false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + path.hashCode();
        return result;
    }
}
