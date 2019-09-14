package com.example.mike.mp3player.service.library.search;

import androidx.annotation.NonNull;
import androidx.room.PrimaryKey;

import org.apache.commons.lang3.StringUtils;

import java.util.Locale;

public abstract class SearchEntity {

    @PrimaryKey
    @NonNull
    private final String id;

    @NonNull
    private final String value;

    public SearchEntity(final String id, final String value) {
        this.id = id;
        this.value = normalise(value);
    }

    public String getValue() {
        return value;
    }

    public String getId() {
        return id;
    }

    private String normalise(String value) {
        String toReturn = StringUtils.stripAccents(value);
        return toReturn.toUpperCase(Locale.getDefault());
    }
}
