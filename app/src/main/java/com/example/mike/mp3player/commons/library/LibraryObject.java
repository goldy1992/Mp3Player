package com.example.mike.mp3player.commons.library;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

import com.example.mike.mp3player.client.Category;
import com.example.mike.mp3player.commons.MediaItemType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class LibraryObject {

    private final MediaItemType parentType;
    private final String id;
    private String title;
    private HashMap<String, String> extras;


    public LibraryObject(MediaItemType mediaItemType, @Nullable String id) {
        this.parentType = mediaItemType;
        this.id = id;
        this.setTitle(null);
        this.extras = new HashMap<>();
    }


    public MediaItemType getParentType() {
        return parentType;
    }

    public String getId() {
        return id;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
    //    sb.append("id: ").append(id).append(", category: ").append(category);
        return sb.toString();
    }

    public HashMap<String, String> getExtras() {
        return extras;
    }

    public boolean hasExtra(String key) {
        return extras.containsKey(key);
    }

    public String getExtra(String key) {
        return extras.get(key);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
