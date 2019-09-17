package com.example.mike.mp3player.service.library.search;

import androidx.annotation.NonNull;
import androidx.room.PrimaryKey;

public abstract class SearchEntity {

    @PrimaryKey
    @NonNull
    private final String id;

    @NonNull
    private final String value;

    public SearchEntity(final String id, final String value) {
        this.id = id;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String getId() {
        return id;
    }

}
