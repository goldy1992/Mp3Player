package com.example.mike.mp3player.service.library.search;

import androidx.annotation.NonNull;
import androidx.room.PrimaryKey;

public abstract class SearchEntity {

    @PrimaryKey
    @NonNull
    private final String id;

    private String value;

    public SearchEntity(final String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getId() {
        return id;
    }
}
