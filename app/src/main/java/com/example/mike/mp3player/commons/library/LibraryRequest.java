package com.example.mike.mp3player.commons.library;

import android.os.Parcel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mike.mp3player.client.Category;
import com.example.mike.mp3player.commons.MediaItemType;

public class LibraryRequest extends LibraryObject {


    public LibraryRequest(@NonNull MediaItemType category, @Nullable String id) {
        super(category, id);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        return sb.toString();
    }

}